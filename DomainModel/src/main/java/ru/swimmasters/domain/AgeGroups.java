package ru.swimmasters.domain;

import java.util.List;

/**
 * TODO: specify intersection contract?
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroups {
    // TODO: we need root references to clarify contract on all containing elements?
    Event getEvent();

    List<AgeGroup> getAllSortedByAge();

    /**
     * Finds appropriate group for Athlete or throws IllegalArgumentException.
     */
    AgeGroup getFor(Athlete athlete);

    boolean canParticipate(Athlete athlete);
}
