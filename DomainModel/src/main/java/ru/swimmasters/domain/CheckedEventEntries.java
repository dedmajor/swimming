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

    public CheckedEventEntries(List<? extends Entry> entries) {
        this.entries = new ArrayList<Entry>(entries);
    }

    @NotNull
    @Override
    public List<Entry> getAll() {
        return entries;
    }

    @NotNull
    @Override
    public EventEntries getRegular() {
        List<Entry> result = new ArrayList<Entry>();
        for (Entry entry : entries) {
            if (entry.getStatus() == EntryStatus.REGULAR) {
                result.add(entry);
            }
        }
        return new CheckedEventEntries(result);
    }

    @Override
    public List<Heat> getHeatsOrderedByNumber() {
        checkHeatsPrepared();
        SortedSet<Heat> sortedHeats = new TreeSet<Heat>(new Comparator<Heat>() {
            @Override
            public int compare(Heat o1, Heat o2) {
                return Integer.valueOf(o1.getNumber()).compareTo(o2.getNumber());
            }
        });
        for (Entry entry : entries) {
            sortedHeats.add(entry.getHeat());
        }
        return new ArrayList<Heat>(sortedHeats);
    }

    @Override
    public boolean isAllHeatsCompetitive() {
        checkHeatsPrepared();
        for (Entry entry : entries) {
            Heat heat = entry.getHeat();
            assert heat != null;
            if (!heat.isCompetitive()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Event getEvent() {
        checkTheSameEvent();
        if (entries.isEmpty()) {
            throw new IllegalStateException("entries list is empty, so cannot determine event from it");
        }
        return entries.get(0).getEvent();
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
                        return entries;
                    }
                });
            }
        }
        return result;
    }

    @Override
    public boolean isStartListPrepared() {
        for (Entry entry : entries) {
            if (!entry.isHeatPrepared()) {
                return false;
            }
        }
        return true;
    }

    private void checkHeatsPrepared() {
        checkTheSameEvent();
        if (!isStartListPrepared()) {
            throw new IllegalStateException("heats are not prepared");
        }
    }

    private boolean isOfTheSameEvent() {
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
