package ru.swimmasters.domain;

/**
 * Race is also known as a heat.
 *
 * User: dedmajor
 * Date: 5/15/11
 */
public interface RaceSensorsListener {
    /**
     * Triggered exactly one time when the referee presses the start button.
     */
    void onRaceStarted();

    /**
     * Can happen before the race is started indicating the false start.
     */
    void onLaneStarted(int lane);

    /**
     * Triggered when the athlete touches the lap sensor.
     */
    void onLapFinished(LapResult lapResult);

    /**
     * Triggered after the last lap at each lane (an athlete finishes).
     */
    void onLaneFinished(FinalLaneResult finalLaneResult);

    /**
     * Triggered exactly one time when the finish of the race is registered
     * (some athletes may not finish).
     */
    void onRaceFinished(RaceResult raceResult);
}
