package ru.swimmasters.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.Clock;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public class SwimMastersRaceRunner implements RaceRunner {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Clock clock;

    public SwimMastersRaceRunner(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void startRace(Heat heat) {
        SwimMastersHeat ourHeat = (SwimMastersHeat) heat;
        ourHeat.setRaceStatus(RaceStatus.IN_PROGRESS);
        ourHeat.setStartTimestamp(clock.now());
    }

    @Override
    public Result registerLaneResult(Heat heat, FinalLaneResult finalLaneResult) {
        checkHeatInProgress(heat);
        logger.debug("registering result " + finalLaneResult + " for heat " + heat);
        SwimMastersEntry entry = (SwimMastersEntry) heat.getEntryByLane(finalLaneResult.getLaneNumber());
        SwimMastersResult result = new SwimMastersResult(entry);
        entry.setResult(result);
        return result;
    }

    @Override
    public List<Result> registerAllLaneResults(Heat heat, LaneResults laneResults) {
        List<Result> returnValue = new ArrayList<Result>();
        for (FinalLaneResult result : laneResults.getAll()) {
            returnValue.add(registerLaneResult(heat, result));
        }
        return returnValue;
    }

    private static void checkHeatInProgress(Heat heat) {
        if (heat.getRaceStatus() != RaceStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("heat is not in progress");
        }
    }

    @Override
    public void finishRace(Heat heat) {
        checkHeatInProgress(heat);

        SwimMastersHeat ourHeat = (SwimMastersHeat) heat;
        ourHeat.setRaceStatus(RaceStatus.FINISHED);
        ourHeat.setFinishTimestamp(clock.now());
    }

    @Override
    public void importRace(Heat heat, RaceResult result) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addSensorsListener(Heat heat, RaceSensorsListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
