package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of {@link EventEntries} interface which ensures elements
 * to be of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public class CheckedEventEntries implements EventEntries {
    private final Event event;
    private final List<Entry> entries;
    private static final Comparator<Entry> ENTRIES_COMPARATOR = new EntryAthleteNameComparator();

    public CheckedEventEntries(Event event, List<? extends Entry> entries) {
        for (Entry entry : entries) {
            if (!entry.getEvent().equals(event)) {
                throw new IllegalArgumentException("entry " + entry + " doesn't belong to event " + event);
            }
        }
        this.entries = new ArrayList<Entry>(entries);
        this.event = event;
    }

    @NotNull
    @Override
    public List<Entry> getAll() {
        return entries;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public List<Entry> getAllSortedByAthleteName() {
        List<Entry> result = entries;
        Collections.sort(result, ENTRIES_COMPARATOR);
        return result;
    }

    @Override
    public Set<Heat> getHeats() {
        Set<Heat> result = new HashSet<Heat>();
        for (Entry entry : entries) {
            result.add(entry.getHeat());
        }
        return result;
    }

    @Override
    public Map<AgeGroup, Entries> getGroupedByAge() {
        Map<AgeGroup, Entries> result = new TreeMap<AgeGroup, Entries>();
        Map<AgeGroup, ArrayList<Entry>> map = new HashMap<AgeGroup, ArrayList<Entry>>();
        for (Entry entry : entries) {
            if (map.containsKey(entry.getAgeGroup())) {
                map.get(entry.getAgeGroup()).add(entry);
            } else {
                final ArrayList<Entry> entries = new ArrayList<Entry>();
                entries.add(entry);
                map.put(entry.getAgeGroup(), entries);
                result.put(entry.getAgeGroup(), new Entries() {
                    @NotNull
                    @Override
                    public List<Entry> getAll() {
                        return Collections.unmodifiableList(entries);
                    }
                });
            }
        }
        return result;
    }
}
