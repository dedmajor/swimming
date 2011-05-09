package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * Checks that all entries have Heat and lane information, i. e. prepared with
     * {@link ru.swimmasters.service.StartListBuilder}.
     */
    boolean isStartListPrepared();

    /**
     * @return the date when the latest start list have been prepared
     */
    @Nullable
    DateTime getStartListTimestamp();

    /**
     * @return entries with the regular status
     */
    @NotNull
    EventEntries getStartListEntries();

    /**
     * @return heats containing start list entries
     */
    @NotNull
    StartListHeats getStartListHeats();

    AgeGroups getAgeGroups();

    Pool getPool();
}
