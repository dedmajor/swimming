package ru.swimmasters.domain;

import java.util.Map;

/**
 * Entries in the context of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface EventEntries extends Entries {
    Event getEvent();

    /**
     * Checks that all entries have Heat and lane information, i. e. prepared with
     * {@link ru.swimmasters.service.HeatBuilderService}.
     */
    boolean isHeatsPrepared();

    Map<AgeGroup, EventEntries> getGroupedByAge();

    /**
     * Maps the heat number (from 1 to all.size()) to the list of entries
     * ordered by a lane number.
     *
     * Entries MUST have prepared heats, otherwise IllegalStateException is thrown.
     */
    Map<Heat, Entries> getGroupedByHeats();

    /**
     * Check that each prepared heat is competitive.
     */
    boolean isAllHeatsCompetitive();
}
