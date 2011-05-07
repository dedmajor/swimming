package ru.swimmasters.service;

import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.Event;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface StartListBuilder {
    /**
     * Prepares and sets {@link Heat} and lane information for each {@link Entry}
     * with the regular status.
     * Event MUST have at least one entry, otherwise IllegalArgumentException is thrown.
     */
    void buildHeats(Event event);
}
