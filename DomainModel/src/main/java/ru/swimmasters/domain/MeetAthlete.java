package ru.swimmasters.domain;

/**
 * An athlete in a context of a single meet.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface MeetAthlete extends Athlete {
    // Meet getMeet();
    Entries getEntries();
}
