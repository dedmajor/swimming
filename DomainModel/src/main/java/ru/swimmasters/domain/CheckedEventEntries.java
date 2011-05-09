package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of {@link EventEntries} interface which checks elements
 * to be of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public class CheckedEventEntries implements EventEntries {
    private final List<Entry> entries;
    private static final Comparator<Entry> ENTRIES_COMPARATOR = new EntryAthleteNameComparator();

    public CheckedEventEntries(List<? extends Entry> entries) {
        this.entries = new ArrayList<Entry>(entries);
    }

    @NotNull
    @Override
    public List<Entry> getAll() {
        return entries;
    }

    @Override
    public Event getEvent() {
        checkTheSameEvent();
        return entries.get(0).getEvent();
    }

    @Override
    public List<Entry> getAllSortedByAthleteName() {
        List<Entry> result = entries;
        Collections.sort(result, ENTRIES_COMPARATOR);
        return result;
    }

    @Override
    public Map<AgeGroup, Entries> getGroupedByAge() {
        checkTheSameEvent();
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

    private boolean isOfTheSameEvent() {
        if (entries.isEmpty()) {
            throw new IllegalStateException("entries list is empty, so cannot determine event from it");
        }
        Event event = null;
        for (Entry entry : entries) {
            if (event == null) {
                event = entry.getEvent();
            } else {
                if (!entry.getEvent().equals(event)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkTheSameEvent() {
        if (!isOfTheSameEvent()) {
            throw new IllegalStateException("all heats must be of the same event");
        }
    }
}
