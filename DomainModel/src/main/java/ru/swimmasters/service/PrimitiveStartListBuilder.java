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
                ? event.getStartListHeats().getHeatsOrderedByNumber()
                : Collections.<Heat>emptyList();

        Map<AgeGroup, Entries> groupedByAge = entries.getGroupedByAge();
        List<AgeGroup> groups = event.getAgeGroups().getAllOrderedByAge();
        Collections.reverse(groups);
        int heatNumber = 1;
        SwimMastersHeat heat = new SwimMastersHeat();
        int lane = event.getPool().getLaneMin();
        for (AgeGroup group : groups) {
            List<Entry> groupEntries = new ArrayList<Entry>(groupedByAge.get(group).getAll());
            Collections.sort(groupEntries, SwimMastersEntry.heatEntryComparator());
            Collections.reverse(groupEntries);
            if (heat.hasNumber()) {
                lane = event.getPool().getLaneMin();
                heat = new SwimMastersHeat();
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
                    heat = new SwimMastersHeat();
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
