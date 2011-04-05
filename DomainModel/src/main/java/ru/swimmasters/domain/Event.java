package ru.swimmasters.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Event {
    Long getId();
    EventGender getEventGender();
    SwimStyle getSwimStyle();
    LocalDate getDate();

    EventEntries getEntries();
    DateTime getStartListTimestamp();

    AgeGroups getAgeGroups();

    Pool getPool();
}
