package ru.swimmasters.domain;

import org.joda.time.Duration;

/**
 * Stramatel:
 *
 * &lt;structTime&gt;
 *     &lt;iState&gt;NA&lt;/iState&gt;
 *     &lt;iLength&gt;100&lt;/iLength&gt;
 *     &lt;iSensor&gt;0&lt;/iSensor&gt;
 *     &lt;tsTime&gt;00:00:00.00&lt;/tsTime&gt;
 * &lt;/structTime&gt;
 * 
 * User: dedmajor
 * Date: 5/15/11
 */
public interface LapSensorTime {
    /**
     * @return sensor number unique within one lane
     */
    int getSensorNumber();
    SensorState getSensorState();

    /**
     * @return true if the sensor in the {@link SensorState#MAIN_MODE}.
     */
    boolean isTimeAvailable();

    /**
     * Split length is the integral (accumulated) lap time (i. e. always grow
     * in laps of a same heat).
     */
    int getSplitLength();

    Duration getSplitTime();
}
