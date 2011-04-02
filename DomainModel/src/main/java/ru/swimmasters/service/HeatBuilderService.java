package ru.swimmasters.service;

import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface HeatBuilderService {
    /**
     * Prepares and sets {@link Heat} and lane information for each {@link Entry}.
     */
    void buildHeats(EventEntries entries);
}
