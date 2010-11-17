package ru.swimmasters.domain;

import java.util.*;

/**
 * TODO: holds heats for particular event
 *
 * User: dedmajor
 * Date: Nov 13, 2010
 */
public class StartList {
    private final Event event;
    private SortedSet<Heat> heats = new TreeSet<Heat>();

    public StartList(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void addHeats(List<Heat> heats) {
        this.heats.addAll(heats);
    }

    public SortedSet<Heat> getHeats() {
        return Collections.unmodifiableSortedSet(heats);
    }
}
