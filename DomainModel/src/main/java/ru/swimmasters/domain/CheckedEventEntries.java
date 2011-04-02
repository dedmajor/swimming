package ru.swimmasters.domain;

import java.util.Map;

/**
 * Default implementation of {@link EventEntries} interface which checks elements
 * to be of the same event.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public abstract class CheckedEventEntries implements EventEntries {
    @Override
    public Map<Heat, Entries> getGroupedByHeats() {
        checkHeatsPrepared();
        return null;
    }

    @Override
    public Map<AgeGroup, EventEntries> getGroupedByAge() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isAllHeatsCompetitive() {
        checkHeatsPrepared();
        for (Entry entry : getAll()) {
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
        return getAll().get(0).getEvent();
    }

    @Override
    public boolean isHeatsPrepared() {
        for (Entry entry : getAll()) {
            if (!entry.isHeatPrepared()) {
                return false;
            }
        }
        return true;
    }

    private void checkHeatsPrepared() {
        checkTheSameEvent();
        if (!isHeatsPrepared()) {
            throw new IllegalStateException("heats are not prepared");
        }
    }

    private boolean isOfTheSameEvent() {
        Event event = null;
        for (Entry entry : getAll()) {
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
