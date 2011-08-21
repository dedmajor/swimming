package ru.swimmasters.service;

import ru.swimmasters.domain.*;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public interface RaceRunner {
    /**
     * The start button of the race. Moves the status to {@link RaceStatus#IN_PROGRESS}
     * and sets the corresponding start timestamp.
     *
     * @see Heat#getStartTimestamp()
     */
    void startRace(Heat heat);

    /**
     * Creates or updates the result entry to the athlete of the heat being in progress now.
     *
     * @see Result
     */
    Result registerLaneResult(Heat heat, FinalLaneResult finalLaneResult);

    List<Result> registerAllLaneResults(Heat heat, LaneResults laneResults);

    /**
     * The finish button of the race. Moves the status to {@link RaceStatus#FINISHED}
     * and sets the corresponding finish timestamp.
     *
     * @see Heat#getFinishTimestamp()
     */
    void finishRace(Heat heat);

    /**
     * Starts the race, registers lane results and finished the race at once.
     * Used for importing the data from external systems.
     */
    void importRace(Heat heat, RaceResult result);

    void addSensorsListener(Heat heat, RaceSensorsListener listener);
}
