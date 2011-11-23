package ru.swimmasters.barcode;

import javax.media.*;
import java.io.File;
import java.io.IOException;

/**
 * User: dedmajor
 * Date: 11/12/11
 */
public class Capture {
    public static void main(String[] args) {
        File workingDirectory = new File("/dev/shm/workdir");
        // TODO: keep files if nothing recognized?
        workingDirectory.mkdir();
        CaptureService service = new WebcamDatamatrixCaptureService(
                workingDirectory);
        String text = service.capture(1000);
        for (File file : workingDirectory.listFiles()) {
            file.deleteOnExit();
        }
        System.out.println(text);
    }
}
