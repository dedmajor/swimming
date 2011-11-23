package ru.swimmasters.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.Clock;

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
 * Date: 4/3/11
 */
public class SwimMastersStartListBuilder implements StartListBuilder {
    private final Clock clock;
    private int leadsInAgeGroup = 1;

    public SwimMastersStartListBuilder(Clock clock) {
        this.clock = clock;
    }

    public void setLeadsInAgeGroup(int leadsInAgeGroup) {
        this.leadsInAgeGroup = leadsInAgeGroup;
    }

    @Override
    public List<Heat> buildHeats(Event event) {
        // TODO: FIXME: check that it's alreay empty, don't return a value
        List<Heat> result = event.isStartListPrepared()
                ? event.getStartListHeats().getAllSortedByNumber()
                : Collections.<Heat>emptyList();

        EventEntries entries = event.getStartListEntries();

        if (entries.getAllSortedByAthleteName().isEmpty()) {
            throw new IllegalArgumentException("event contain no regular entries");
        }

        // TODO: FIXME: check that race status is not_started

        ((SwimMastersEvent) event).setStartListTimestamp(clock.now());

        Map<AgeGroup, AgeQueue> queues = buildAgeQueues(entries);

        List<AgeGroup> groups = new ArrayList<AgeGroup>(event.getAgeGroups().getAllSortedByAge());
        Collections.reverse(groups);

        SwimMastersHeat currentHeat = null;
        SwimMastersHeat previousBrickHeat = null;

        // TODO: FIXME: and absolute?
        int number = 0;

        for (AgeGroup group : groups) {
            AgeQueue queue = queues.get(group);
            if (queue == null) {
                continue;
            }
            while (queue.hasMoreEntries()) {
                previousBrickHeat = currentHeat;

                if (currentHeat == null || !hasMoreSpace(event, currentHeat, queue.nextBrickSize())) {
                    assert currentHeat == null || currentHeat.isCompetitive();
                    number++;
                    currentHeat = new SwimMastersHeat((SwimMastersEvent) event);
                    currentHeat.setNumber(number);
                }

                linkHeatToBrick(event, currentHeat, queue.nextBrick());
            }
        }

        if (!(currentHeat == null || currentHeat.isCompetitive() || previousBrickHeat == null)) {
            currentHeat.addBrick(previousBrickHeat.removeLastAddedBrick());
        }
        return result;
    }

    @Override
    public List<Heat> prepareHeats(Meet meet) {
        List<Heat> result = new ArrayList<Heat>();
        int absoluteNumber = 0;
        for (Event event : meet.getEvents().getAll()) {
            if (event.getStartListEntries().getAllSortedByAthleteName().isEmpty()) {
                continue;
            }
            List<Heat> eventResult = buildHeats(event);
            result.addAll(eventResult);
            absoluteNumber = setAbsoluteNumber(absoluteNumber, event);
        }
        return result;
    }

    private static int setAbsoluteNumber(int absoluteNumber, Event event) {
        int result = absoluteNumber;
        for (Heat heat : event.getStartListHeats().getAllSortedByNumber()) {
            ++result;
            ((SwimMastersHeat) heat).setAbsoluteNumber(result);
        }
        return result;
    }

    private static void linkHeatToBrick(Event event, SwimMastersHeat currentHeat, List<SwimMastersEntry> nextBrick) {
        int lane = event.getPool().getLaneMin() + currentHeat.getEntries().getAllSortedByLane().size();
        for (SwimMastersEntry brickEntry : nextBrick) {
            brickEntry.setHeat(currentHeat);
            brickEntry.setLane(lane);
            lane++;
        }
        currentHeat.addBrick(nextBrick);
    }

    private static boolean hasMoreSpace(Event event, Heat currentHeat, int size) {
        return currentHeat.getEntries().getAllSortedByLane().size() + size
                <= event.getPool().getMeetLanesCount();
    }

    /**
     * @return age queues ordered by their age group, from youngest to oldest
     */
    private Map<AgeGroup, AgeQueue> buildAgeQueues(EventEntries entries) {
        Map<AgeGroup, AgeQueue> result = new TreeMap<AgeGroup, AgeQueue>();

        for (Entry entry : entries.getAllSortedByAthleteName()) {
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
