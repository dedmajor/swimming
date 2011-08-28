package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroups {
    Event getEvent();

    List<AgeGroup> getAllOrderedByAge();

    /**
     * Finds appropriate group for Athlete or throws IllegalArgumentException.
     */
    AgeGroup getFor(Athlete athlete);

    boolean canParticipate(Athlete athlete);
}
