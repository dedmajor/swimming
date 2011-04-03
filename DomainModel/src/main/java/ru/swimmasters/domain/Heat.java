package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Heat {
    int getNumber();

    /**
     * All entries of this heat (i. e. {@link Entry#getHeat() equals this heat)}.
     */
    Entries getEntries();

    /**
     * @return true if there is more than one entry, false otherwise
     */
    boolean isCompetitive();

    /**
     * Returns the youngest group (i. e. with the minimal age).
     */
    AgeGroup getOldestAgeGroup();

    /**
     * Returns the oldest group (i. e. with the maximum age).
     */
    AgeGroup getYoungestAgeGroup();

    /**
     * Returns the fastest entry (i. e. with the minimal entry time) in the specified group.
     *
     * Throws IllegalArgumentException if the heat have no entries of this age group.
     */
    Entry getFastestEntry(AgeGroup ageGroup);
}
