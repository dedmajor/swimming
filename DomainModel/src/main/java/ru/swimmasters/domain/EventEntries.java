package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Entries in the context of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface EventEntries extends Entries {
    Event getEvent();

    Map<AgeGroup, Entries> getGroupedByAge();

    /**
     * Checks that all entries have Heat and lane information, i. e. prepared with
     * {@link ru.swimmasters.service.StartListBuilder}.
     */
    boolean isStartListPrepared();

    /**
     * Entries MUST have prepared heats, otherwise IllegalStateException is thrown.
     */
    List<Heat> getHeatsOrderedByNumber();

    /**
     * Check that each prepared heat is competitive.
     */
    boolean isAllHeatsCompetitive();

    /**
     * @return entries with the regular status only
     */
    @NotNull
    EventEntries getRegular();
}
