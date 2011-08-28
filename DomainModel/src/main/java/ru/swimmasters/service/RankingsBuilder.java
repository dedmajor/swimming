package ru.swimmasters.service;

import ru.swimmasters.domain.AgeRankings;
import ru.swimmasters.domain.Event;

/**
 * User: dedmajor
 * Date: 8/21/11
 */
public interface RankingsBuilder {
    /**
     * All events MUST be finished. Builds and updates age group rankings
     * as well as rankings timestamp.
     *
     * @see Event#isAllHeatsFinished
     */
    AgeRankings buildEventAgeRankings(Event event);
}
