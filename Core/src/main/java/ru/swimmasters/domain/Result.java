package ru.swimmasters.domain;

import javax.validation.constraints.Digits;

/**
 * User: dedmajor
 * Date: Nov 17, 2010
 */
public class Result implements Comparable<Result> {
    private final Application application;
    private Heat heat;

    private ResultCode code = ResultCode.DNS;

    @Digits(integer = 5, fraction = 2)
    private Float swimTime; // TODO: splits

    private Float reactionTime;
    private int points; // TODO: just a cache?


    public Result(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }

    public ResultCode getCode() {
        return code;
    }

    public Float getSwimTime() {
        return swimTime;
    }

    public void setHeat(Heat heat) {
        if (!heat.containsApplication(application)) {
            throw new IllegalArgumentException("heat " + heat + " doesn't contain application " + application);
        }
        this.heat = heat;
    }

    /**
     * If result should have swim time, then swimTime should not be null;
     */
    public void setResult(ResultCode code, Float swimTime) {
        if (!isValidResult(code, swimTime)) {
            throw new IllegalArgumentException("swim time is only for finished result");
        }
        this.code = code;
        this.swimTime = swimTime;
    }

    public boolean isValidResult(ResultCode result, Float swimTime) {
        return result.shouldHaveSwimTime() == (swimTime != null);
    }

    public int getLaneNumber() {
        return getLane().getNumber();
    }

    public int getHeatNumber() {
        if (heat == null) {
            throw new IllegalStateException("heat is unknown");
        }
        return heat.getNumber();
    }

    public Heat getHeat() {
        return heat;
    }

    public Lane getLane() {
        if (heat == null) {
            throw new IllegalStateException("heat is unknown");
        }
        return heat.getLaneByApplication(application);
    }

    @Override
    public int compareTo(Result o) {
        if (code != ResultCode.FINISHED && o.getCode() != ResultCode.FINISHED) return 0;
        if (code == ResultCode.FINISHED && o.getCode() != ResultCode.FINISHED) return -1;
        if (code != ResultCode.FINISHED && o.getCode() == ResultCode.FINISHED) return 1;

        return swimTime.compareTo(o.getSwimTime()); // TODO: FIXME: compare by points?
    }
}
