package ru.swimmasters.domain;

import org.joda.time.Duration;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Entry {
    MeetAthlete getAthlete();
    Event getEvent();
    Duration getEntryTime();
}