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

    Map<AgeGroup, Entries> getGroupedByAge();
}
