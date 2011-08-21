package ru.swimmasters.service;

import ru.swimmasters.domain.Event;
import ru.swimmasters.domain.SwimMastersEvent;
import ru.swimmasters.time.Clock;

/**
 * User: dedmajor
 * Date: 8/21/11
 */
public class SwimMastersRankingsBuilder implements RankingsBuilder {
    private final Clock clock;

    public SwimMastersRankingsBuilder(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void buildEventAgeRankings(Event event) {
        if (!event.isAllHeatsFinished()) {
            throw new IllegalArgumentException("not all heats are finished!");
        }
        ((SwimMastersEvent) event).setRankingsTimestamp(clock.now());
        // TODO: set rankings
    }
}
