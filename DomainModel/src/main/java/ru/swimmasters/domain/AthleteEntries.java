package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

/**
 * User: dedmajor
 * Date: 11/20/11
 */
public interface AthleteEntries {
    // TODO: we need root references to clarify contract on all containing elements?
    MeetAthlete getAthlete();
    Meet getMeet();

    /**
     * @return all entries of the athlete in the meet.
     */
    @NotNull
    Iterable<Entry> getAll();
}
