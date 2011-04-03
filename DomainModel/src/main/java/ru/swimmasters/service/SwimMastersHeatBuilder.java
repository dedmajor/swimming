package ru.swimmasters.service;

import ru.swimmasters.domain.*;

import java.util.*;

/**
 * Heat builder which MUST keep leaders (the fastest athletes) of the same age group together
 * in the best heat (i. e. the last heat, heat with the maximum number) of each group.
 *
 * Younger groups MUST swim later in time (i. e. in greater heat numbers).
 *
 * Faster sportsmen MUST swim later in each group (i. e. in the greater heat number).
 *
 * Several groups CAN swim together in the same heat.
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
        Map<AgeGroup, Entries> groupedByAge = entries.getGroupedByAge();
        List<AgeGroup> groups = entries.getEvent().getAgeGroups().getAllOrderedByAge();
        Collections.reverse(groups);
        int heatNumber = 1;
        for (AgeGroup group : groups) {
            List<Entry> groupEntries = new ArrayList<Entry>(groupedByAge.get(group).getAll());
            Collections.sort(groupEntries, SwimMastersEntry.heatEntryComparator());
            Collections.reverse(groupEntries);
            for (Entry entry : groupEntries) {
                SwimMastersHeat heat = new SwimMastersHeat();
                heat.setNumber(heatNumber);
                heatNumber++;
                SwimMastersEntry ourEntry = (SwimMastersEntry) entry;
                ourEntry.setHeat(heat);
                ourEntry.setLane(entries.getEvent().getPool().getLaneMax());
                heat.entries.add(ourEntry);
            }
        }
    }

    public void setLeadsInAgeGroup(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }
}
