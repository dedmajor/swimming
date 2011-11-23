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
    private final Event event;
    private final CheckedEventEntries entries;

    public CheckedStartListHeats(Event event, CheckedEventEntries entries) {
        this.event = event;
        this.entries = entries;
    }

    @Override
    public List<Heat> getAllSortedByNumber() {
        checkStartListPrepared();
        List<Heat> sortedHeats = new ArrayList<Heat>(entries.getHeats());
        Collections.sort(sortedHeats, new Comparator<Heat>() {
            @Override
            public int compare(Heat o1, Heat o2) {
                return Integer.valueOf(o1.getNumber()).compareTo(o2.getNumber());
            }
        });
        return sortedHeats;
    }

    private void checkStartListPrepared() {
        if (!event.isStartListPrepared()) {
            throw new IllegalStateException("heats are not prepared");
        }
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public boolean isAllHeatsCompetitive() {
        checkStartListPrepared();
        for (Entry entry : entries.getAllSortedByAthleteName()) {
            Heat heat = entry.getHeat();
            assert heat != null;
            if (!heat.isCompetitive()) {
                return false;
            }
        }
        return true;
    }
}
