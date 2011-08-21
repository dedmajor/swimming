package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.Duration;

import java.util.Collections;
import java.util.List;

/**
 * The manual result without splits.
 *
 * User: dedmajor
 * Date: 5/15/11
 */
public class ManualLaneResult implements FinalLaneResult {
    private int laneNumber;
    private Duration finalTime;

    public ManualLaneResult() {
    }

    public ManualLaneResult(int laneNumber, Duration finalTime) {
        this.laneNumber = laneNumber;
        this.finalTime = finalTime;
    }

    public ManualLaneResult(Entry entry) {
        if (!entry.isHeatPrepared()) {
            throw new IllegalArgumentException("heats in entry " + entry + " are not prepared yet");
        }
        Integer lane = entry.getLane();
        assert lane != null;
        laneNumber = lane;
        Result result = entry.getResult();
        if (result != null) {
            finalTime = result.getSwimTime();
        }
    }

    @Override
    public int getLaneNumber() {
        return laneNumber;
    }

    @Override
    public Duration getFinalTime() {
        return finalTime;
    }

    public void setLaneNumber(int laneNumber) {
        this.laneNumber = laneNumber;
    }

    public void setFinalTime(Duration finalTime) {
        this.finalTime = finalTime;
    }

    @Override
    public int getLapsCount() {
        return 0;
    }

    @Override
    public LapResults getLapResults() {
        return new LapResults() {
            @Override
            public List<LapResult> getAll() {
                return Collections.emptyList();
            }
        };
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).
                append("laneNumber", laneNumber).
                append("finalTime", finalTime).
                toString();
    }
}
