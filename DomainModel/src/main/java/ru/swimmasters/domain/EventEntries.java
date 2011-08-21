package ru.swimmasters.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Entries in the context of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface EventEntries extends Entries {
    Event getEvent();

    List<Entry> getAllSortedByAthleteName();

    Set<Heat> getHeats();

    Map<AgeGroup, Entries> getGroupedByAge();
}
