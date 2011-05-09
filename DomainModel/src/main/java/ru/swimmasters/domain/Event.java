package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;
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
    int getNumber();

    @NotNull
    EventEntries getEntries();

    /**
     * @return entries with the regular status only
     */
    @NotNull
    EventEntries getRegularEntries();

    DateTime getStartListTimestamp();

    AgeGroups getAgeGroups();

    Pool getPool();
}
