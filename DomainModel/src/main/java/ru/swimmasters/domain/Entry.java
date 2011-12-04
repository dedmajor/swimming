package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;

import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Entry {
    // TODO: constraint, depends on event.isRelayEvent / isIndividualEvent
    @NotNull
    MeetAthlete getAthlete();
    RelayTeam getRelayTeam();

    Club getClub();

    @NotNull
    Event getEvent();
    // TODO: if relay event, constraint: event.swimstyle.relayscount = relayteam.getSize()

    /**
     * This is used for the entry status information.
     */
    EntryStatus getStatus();

    Duration getEntryTime();

    // TODO: qualifying time?

    // TODO: PreparedHeat, not null
    @Nullable
    Heat getHeat();
    @Nullable
    Integer getLane();

    /**
     * @return true if heat and lane are set, false otherwise
     */
    boolean isHeatPrepared();

    AgeGroup getAgeGroup();

    // TODO: not null, throw exception
    @Nullable
    Result getResult();
}
