package ru.swimmasters.domain;

import java.util.*;

/**
 * The implementation of the StartListHeats which checks
 * the start list for the preparation status.
 *
 * User: dedmajor
 * Date: 5/9/11
 */
public class CheckedStartListHeats implements StartListHeats {
    private final CheckedEventEntries entries;

    public CheckedStartListHeats(CheckedEventEntries entries) {
        this.entries = entries;
    }

    @Override
    public List<Heat> getHeatsOrderedByNumber() {
        checkStartListPrepared();
        SortedSet<Heat> sortedHeats = new TreeSet<Heat>(new Comparator<Heat>() {
            @Override
            public int compare(Heat o1, Heat o2) {
                return Integer.valueOf(o1.getNumber()).compareTo(o2.getNumber());
            }
        });
        for (Entry entry : entries.getAll()) {
            sortedHeats.add(entry.getHeat());
        }
        return new ArrayList<Heat>(sortedHeats);
    }

    private void checkStartListPrepared() {
        if (!getEvent().isStartListPrepared()) {
            throw new IllegalStateException("heats are not prepared");
        }
    }

    @Override
    public Event getEvent() {
        return entries.getEvent();
    }

    @Override
    public boolean isAllHeatsCompetitive() {
        checkStartListPrepared();
        for (Entry entry : entries.getAll()) {
            Heat heat = entry.getHeat();
            assert heat != null;
            if (!heat.isCompetitive()) {
                return false;
            }
        }
        return true;
    }
}
