package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

/**
 * Also known as a race.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Heat {
    // TODO: replace id with event_id, number?
    Long getId();

    Event getEvent();

    /**
     * @return an unique number within one event
     */
    int getNumber();
    int getTotalHeatsInEvent();

    /**
     * @return an absolute unique number within one meet
     */
    int getAbsoluteNumber();

    /**
     * All entries of this heat (i. e. {@link Entry#getHeat() equals this heat)}.
     */
    Entries getEntries();
    Entry getEntryByLane(int lane);

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

    RaceStatus getRaceStatus();

    /**
     * @return start timestamp if race was started
     */
    @Nullable
    DateTime getStartTimestamp();

    /**
     * @return finish timestamp if race has finished
     */
    @Nullable
    DateTime getFinishTimestamp();
}
