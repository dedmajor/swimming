package ru.swimmasters.service;

import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.SwimMastersEntry;
import ru.swimmasters.domain.SwimMastersHeat;

/**
 * HeatBuilder which keeps leaders of the same age group together in the top heat of each group.
 * Younger groups swim later in time. Fastest (top) sportsmen swim later in each group.
 * Several groups can swim together in the same heat, but age group leads are always in the same heat.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public class SwimMastersHeatBuilder implements HeatBuilderService {
    private int leadsInAgeGroup;

    /**
     * @param entries the collection of {@link SwimMastersEntry} elements.
     */
    @Override
    public void buildHeats(EventEntries entries) {
        SwimMastersHeat heat = new SwimMastersHeat();
        for (Entry entry : entries.getAll()) {
            SwimMastersEntry ourEntry = (SwimMastersEntry) entry;
            ourEntry.setHeat(heat);
            ourEntry.setLane(1);
            heat.entries.add(ourEntry);
        }
    }
}
