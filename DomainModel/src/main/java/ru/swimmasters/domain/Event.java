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
    // TODO: replace id with meet_id, number ?
    Long getId();

    EventGender getEventGender();
    SwimStyle getSwimStyle();

    Session getSession();

    /**
     * @return the date of the session
     */
    LocalDate getDate();

    Meet getMeet();

    Pool getPool();

    /**
     * @return unique number within the meet
     */
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
     * Method does not check that start list is prepared. Use {@link #isStartListPrepared()}
     * beforehand to do this check.
     *
     * @return heats containing start list entries
     */
    @NotNull
    StartListHeats getStartListHeats();

    AgeGroups getAgeGroups();

    /**
     * Checks that all heats are finished.
     */
    boolean isAllHeatsFinished();

    /**
     * @return the date when the latest start list have been prepared
     */
    @Nullable
    DateTime getRankingsTimestamp();
}
