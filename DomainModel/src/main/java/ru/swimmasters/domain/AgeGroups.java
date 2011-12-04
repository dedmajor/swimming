package ru.swimmasters.domain;

import java.util.List;

/**
 * TODO: specify intersection contract?
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroups {
    Event getEvent();

    List<AgeGroup> getAllSortedByAge();

    /**
     * Finds appropriate single group for an athlete or throws IllegalArgumentException.
     */
    AgeGroup getFor(Athlete athlete);
    /**
     * Finds appropriate calculated group for a relay or throws IllegalArgumentException.
     */
    AgeGroup getFor(RelayPositions relayTeam);

    boolean canParticipate(Athlete athlete);
    boolean canParticipate(RelayPositions relayTeam);
}
