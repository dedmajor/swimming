package ru.swimmasters.service;

import ru.swimmasters.domain.*;

import java.util.*;

/**
 * Simple heat builder, which puts younger groups and faster sportsmen in each group later in time
 * (i. e. in the greater heat number).
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public class PrimitiveStartListBuilder implements StartListBuilder {
    private int leadsInAgeGroup;

    /**
     * @param event the event with the collection of {@link ru.swimmasters.domain.SwimMastersEntry} elements.
     */
    @Override
    public List<Heat> buildHeats(Event event) {
        EventEntries entries = event.getEntries();

        List<Heat> result = event.isStartListPrepared()
                ? event.getStartListHeats().getAllSortedByNumber()
                : Collections.<Heat>emptyList();

        Map<AgeGroup, List<Entry>> groupedByAge = entries.getGroupedByAge();
        List<AgeGroup> allowedGroups = event.getAgeGroups().getAllSortedByAge();
        Collections.reverse(allowedGroups);
        int heatNumber = 1;
        SwimMastersEvent ourEvent = (SwimMastersEvent) event;
        SwimMastersHeat heat = new SwimMastersHeat(ourEvent);
        int lane = event.getPool().getLaneMin();
        for (AgeGroup group : allowedGroups) {
            if (groupedByAge.get(group) == null) {
                continue;
            }
            List<Entry> groupEntries = new ArrayList<Entry>(groupedByAge.get(group));
            Collections.sort(groupEntries, SwimMastersEntry.heatEntryComparator());
            Collections.reverse(groupEntries);
            if (heat.hasNumber()) {
                lane = event.getPool().getLaneMin();
                heat = new SwimMastersHeat(ourEvent);
                heatNumber++;
            }
            for (Entry entry : groupEntries) {
                heat.setNumber(heatNumber);
                SwimMastersEntry ourEntry = (SwimMastersEntry) entry;
                ourEntry.setHeat(heat);
                ourEntry.setLane(lane);
                heat.entries.add(ourEntry);

                lane++;
                if (lane > event.getPool().getLaneMax()) {
                    lane = event.getPool().getLaneMin();
                    heat = new SwimMastersHeat(ourEvent);
                    heatNumber++;
                }
            }
        }
        return result;
    }

    @Override
    public List<Heat> prepareHeats(Meet meet) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void setLeadsInAgeGroup(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }
}
