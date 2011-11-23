package ru.swimmasters.barcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: dedmajor
 * Date: 11/12/11
 */
public class WebcamDatamatrixCaptureService implements CaptureService {
    public static final Logger LOGGER = LoggerFactory.getLogger(WebcamDatamatrixCaptureService.class);

    private final File workingDirectory;

    private static final String MPLAYER_FORMAT_PATTERN = "%08d.png";
    private static final int FRAME_LIMIT = 100000000;

    private volatile int handledFrames = 0;

    // calibrated read speed = 40 TPS (spawning one process per each file is enough for 25 FPS)
    private static final int FRAMES_IN_CHUNK = 25; // frames to pass to DTMX
    private static final int FPS = 25;
    private int FRAME_SIZE = 924988;
    private static final int DTMXREAD_TIMEOUT = 30;

    private final AtomicReference<String> resultAtomicReference = new AtomicReference<String>(null);

    private String dmtxreadCommand = "/home/dedmajor/src/dmtx-utils-0.7.4/local/bin/dmtxread";


    public WebcamDatamatrixCaptureService(File workingDirectory) {
        if (!workingDirectory.isDirectory() || !workingDirectory.canWrite()) {
            throw new IllegalArgumentException("working directory is not writable");
        }
        this.workingDirectory = workingDirectory;
    }

    @Override
    public String capture(final int maxFrames) {
        if (maxFrames > FRAME_LIMIT) {
            throw new IllegalArgumentException("max frames " + maxFrames + " but limit is " + FRAME_LIMIT);
        }

        if (handledFrames > 0) {
            throw new IllegalStateException("already captured");
        }

        ProcessBuilder pb = new ProcessBuilder("mplayer",
                "tv://",
                "-frames", String.valueOf(maxFrames),
                "-vo", "png",
                "-fps", String.valueOf(FPS));
        pb.directory(workingDirectory);

        final Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long previousTime = System.currentTimeMillis();
                long timeToWaitForFrame = FRAMES_IN_CHUNK * 1000 / FPS;
                long loopTimePassed = 0;
                while (handledFrames < maxFrames && resultAtomicReference.get() == null) {
                    try {
                        // one chunk lasts for 1000/CPS milliseconds, one frame is 1000/FPS
                        List<File> files = getAvailableFrames(FRAMES_IN_CHUNK, timeToWaitForFrame);
                        LOGGER.debug("available: " + files);
                        if (!files.isEmpty()) {
                            decodeFrames(files);
                        }
                        handledFrames += files.size();

                        long currentTime = System.currentTimeMillis();
                        loopTimePassed = currentTime - previousTime;
                        long loopHandledFrames = (long) files.size();
                        long timeHadToPass = loopHandledFrames * 1000 / FPS;
                        // e. g. handled frames = 2, fps = 25 fps, time had to pass = 2/25 * 1000 millis
                        timeToWaitForFrame = timeHadToPass / 2 - loopTimePassed; // 2 times per frame
                        previousTime = currentTime;
                        LOGGER.debug("time passed: " + loopTimePassed);
                        LOGGER.debug(String.format("frames / time had to pass / passed / delay : % d / %d / %d / %d",
                                loopHandledFrames, timeHadToPass, loopTimePassed, timeToWaitForFrame));
                        if (timeToWaitForFrame < 0) {
                            timeToWaitForFrame = 0;
                        }
                        timeToWaitForFrame = 0; //debug

                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
                process.destroy();
            }
        });
        t.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        workingDirectory.deleteOnExit();


        return null;
    }

    private void decodeFrames(List<File> files) {
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(dmtxreadCommand,
                "-m" + DTMXREAD_TIMEOUT, // max ms
                "-N1", // only first
                "-n"));
        for (File f : files) {
            args.add(f.toString());
        }

        ProcessBuilder pb = new ProcessBuilder(args);
        LOGGER.debug("reading matrix from files " + files);
        Process p;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            LOGGER.debug("reading line from proces");
            String line = readLineFromProcess(p);
            if (line != null) {
                LOGGER.info("recognized the string: " + line);
                resultAtomicReference.set(line);
            } else {
                LOGGER.debug("nothing recognized");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        p.destroy();
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String readLineFromProcess(Process p) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        try {
            return br.readLine();
        } finally {
            br.close();
        }
    }

    private List<File> getAvailableFrames(int framesInChunk, long timeout) throws InterruptedException {
        LOGGER.debug("getting available frames " + framesInChunk);
        List<File> result = new ArrayList<File>(framesInChunk);
        for (int i = 0; i < framesInChunk; i++) {
            int frame = handledFrames + i;
            File nextFrame = new File(workingDirectory, String.format(MPLAYER_FORMAT_PATTERN, frame + 1));
            long frameBytes = nextFrame.length();
            LOGGER.debug("next frame: " + nextFrame + " of size " + frameBytes
                    + " and stamp " + new Date(nextFrame.lastModified()));
            if (!nextFrame.exists() || frameBytes != FRAME_SIZE) {
                Thread.sleep(timeout);
            }
            if (!nextFrame.exists() || frameBytes != FRAME_SIZE) {
                return result;
            }
            result.add(nextFrame);
        }
        return result;
    }
}
