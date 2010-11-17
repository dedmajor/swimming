package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: Nov 9, 2010
 */
public enum ResultCode {
    FINISHED(true),
    DNS;

    private final boolean shouldHaveSwimTime;

    ResultCode() {
        shouldHaveSwimTime = false;
    }

    ResultCode(boolean shouldHaveSwimTime) {
        this.shouldHaveSwimTime = shouldHaveSwimTime;
    }

    public boolean shouldHaveSwimTime() {
        return shouldHaveSwimTime;
    }
}
