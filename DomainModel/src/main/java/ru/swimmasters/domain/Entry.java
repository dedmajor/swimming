package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;

import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Entry {
    @NotNull
    MeetAthlete getAthlete();
    @NotNull
    Event getEvent();

    /**
     * This is used for the entry status information.
     */
    EntryStatus getStatus();

    Duration getEntryTime();

    @Nullable
    Heat getHeat();
    @Nullable
    Integer getLane();

    /**
     * @return true if heat and lane are set, false otherwise
     */
    boolean isHeatPrepared();

    AgeGroup getAgeGroup();

    @Nullable
    Result getResult();
}
