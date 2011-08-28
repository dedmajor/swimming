package ru.swimmasters.service;

import ru.swimmasters.domain.*;
import ru.swimmasters.time.Clock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public AgeRankings buildEventAgeRankings(Event event) {
        if (!event.isAllHeatsFinished()) {
            throw new IllegalArgumentException("not all heats are finished!");
        }
        if (!event.getAgeRankings().getAll().isEmpty()) {
            throw new IllegalArgumentException("age rankings are already set, clean them first");
        }
        SwimMastersEvent ourEvent = (SwimMastersEvent) event;
        ourEvent.setRankingsTimestamp(clock.now());

        List<SwimMastersAgeRanking> ageRankings = new ArrayList<SwimMastersAgeRanking>();

        for (Map.Entry<AgeGroup, Entries> entries : event.getEntries().getGroupedByAge().entrySet()) {
            SwimMastersAgeRanking ageRanking
                    = new SwimMastersAgeRanking((SwimMastersEvent) event, (SwimMastersAgeGroup) entries.getKey());

            List<SwimMastersGroupRanking> groupRankings
                    = new ArrayList<SwimMastersGroupRanking>(entries.getValue().getAll().size());

            for (Entry entry : entries.getValue().getAll()) {
                groupRankings.add(new SwimMastersGroupRanking(ageRanking, (SwimMastersEntry) entry));
            }

            // TODO: sort

            int place = 1;
            for (SwimMastersGroupRanking ranking : groupRankings) {
                // TODO: check status
                ranking.setPlace(place);
                place++;
            }

            ageRanking.setGroupRankings(groupRankings);

            ageRankings.add(ageRanking);
        }

        ourEvent.setAgeRankings(ageRankings);
        return ourEvent.getAgeRankings();
    }
}
