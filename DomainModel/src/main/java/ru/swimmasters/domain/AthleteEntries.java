package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

/**
 * User: dedmajor
 * Date: 11/20/11
 */
public interface AthleteEntries {
    MeetAthlete getAthlete();
    Meet getMeet();

    /**
     * @return all entries of the athlete in the meet.
     */
    @NotNull
    Iterable<Entry> getAll();
}
