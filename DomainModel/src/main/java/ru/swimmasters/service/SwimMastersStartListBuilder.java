package ru.swimmasters.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import ru.swimmasters.domain.*;

import java.util.*;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class SwimMastersStartListBuilder implements StartListBuilder {
    private int leadsInAgeGroup = 1;

    public void setLeadsInAgeGroup(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }

    @Override
    public void buildHeats(EventEntries entries) {
        Map<AgeGroup, AgeQueue> queues = buildAgeQueues(entries);

        List<AgeGroup> groups = new ArrayList<AgeGroup>(entries.getEvent().getAgeGroups().getAllOrderedByAge());
        Collections.reverse(groups);

        SwimMastersHeat currentHeat = null;
        SwimMastersHeat previousBrickHeat = null;
        int number = 0;
        for (AgeGroup group : groups) {
            AgeQueue queue = queues.get(group);
            while (queue.hasMoreEntries()) {
                previousBrickHeat = currentHeat;

                if (currentHeat == null || !hasMoreSpace(entries, currentHeat, queue.nextBrickSize())) {
                    assert currentHeat == null || currentHeat.isCompetitive();
                    number++;
                    currentHeat = new SwimMastersHeat();
                    currentHeat.setNumber(number);
                }

                linkHeatToBrick(entries.getEvent(), currentHeat, queue.nextBrick());
            }
        }

        if (!(currentHeat == null || currentHeat.isCompetitive() || previousBrickHeat == null)) {
            currentHeat.addBrick(previousBrickHeat.removeLastAddedBrick());
        }
    }

    private static void linkHeatToBrick(Event event, SwimMastersHeat currentHeat, List<SwimMastersEntry> nextBrick) {
        int lane = event.getPool().getLaneMin() + currentHeat.getEntries().getAll().size();
        for (SwimMastersEntry brickEntry : nextBrick) {
            brickEntry.setHeat(currentHeat);
            brickEntry.setLane(lane);
            lane++;
        }
        currentHeat.addBrick(nextBrick);
    }

    private static boolean hasMoreSpace(EventEntries entries, Heat currentHeat, int size) {
        return currentHeat.getEntries().getAll().size() + size
                <= entries.getEvent().getPool().getMeetLanesCount();
    }

    /**
     * @return age queues ordered by their age group, from youngest to oldest
     */
    private Map<AgeGroup, AgeQueue> buildAgeQueues(EventEntries entries) {
        Map<AgeGroup, AgeQueue> result = new TreeMap<AgeGroup, AgeQueue>();

        for (Entry entry : entries.getAll()) {
            AgeQueue queue = result.get(entry.getAgeGroup());
            if (queue == null) {
                queue = new AgeQueue(entry.getAgeGroup(), leadsInAgeGroup);
                result.put(entry.getAgeGroup(), queue);
            }

            queue.putEntry((SwimMastersEntry) entry);
        }

        return result;
    }

    /**
     * Ordered queue of applications of the same age group.  First applications in
     * the head of the queue are leads.  Applications are retrieved from the queue
     * inside monolithic bricks. Leads are always in the first brick, all the next
     * bricks are single-sized.
     *
     * @author dedmajor
     * @see SwimMastersEntry#heatEntryComparator() for ordering rules.
     * @since 13.06.2010
     */
    private static class AgeQueue {
        private final AgeGroup ageGroup;
        private final int leadsInAgeGroup;
        private final Queue<SwimMastersEntry> entries;

        private boolean leadsRemoved;

        AgeQueue(AgeGroup ageGroup, int leadsInAgeGroup) {
            this.ageGroup = ageGroup;
            this.leadsInAgeGroup = leadsInAgeGroup;
            leadsRemoved = false;
            entries = new PriorityQueue<SwimMastersEntry>(leadsInAgeGroup,
                    reverseComparator(SwimMastersEntry.heatEntryComparator()));
        }

        public static Comparator<Entry> reverseComparator(final Comparator<Entry> comparator) {
            return new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    return comparator.compare(o2, o1);
                }
            };
        }

        public void putEntry(SwimMastersEntry entry) {
            if (!entry.getAgeGroup().equals(ageGroup)) {
                throw new IllegalArgumentException("entry " + entry
                        + " does not fit into the age group " + ageGroup);
            }
            entries.add(entry);
        }

        public boolean hasMoreEntries() {
            return !entries.isEmpty();
        }

        public List<SwimMastersEntry> nextBrick() {
            int size = nextBrickSize();

            if (size > entries.size()) {
                throw new IllegalArgumentException(
                        "queue has only " + entries.size() + " entries, but requested: " + size);
            }

            List<SwimMastersEntry> result = new ArrayList<SwimMastersEntry>(size);

            for (int i = 0; i < size; i++) {
                result.add(entries.remove());
            }

            leadsRemoved = true;

            return result;
        }

        public int nextBrickSize() {
            return leadsRemoved
                    ? 1
                    : Math.min(entries.size(), leadsInAgeGroup);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).
                    append("ageGroup", ageGroup).
                    append("entries.size", entries.size()).
                    toString();
        }
    }
}
