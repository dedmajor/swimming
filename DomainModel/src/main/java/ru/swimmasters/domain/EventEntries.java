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
public interface EventEntries {
    // TODO: do we need collection know about its parent?
    Event getEvent();

    List<Entry> getAllSortedByAthleteName();

    // TODO: only in StartListEntries??
    // TODO: keep heats only in the start list?
    Set<Heat> getHeats();

    Map<AgeGroup, List<Entry>> getGroupedByAge();
}
