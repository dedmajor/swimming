package ru.swimmasters.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class SwimMastersAgeGroups implements AgeGroups {
    private final Event event;
    private final List<AgeGroup> groups;

    protected SwimMastersAgeGroups(Event event, List<? extends AgeGroup> groups) {
        this.event = event;
        this.groups = new ArrayList<AgeGroup>(groups);
    }

    public static List<SwimMastersAgeGroup> createDefaultGroups() {
        List<SwimMastersAgeGroup> defaultGroups = new ArrayList<SwimMastersAgeGroup>();
        defaultGroups.add(new SwimMastersAgeGroup(0, 19));
        int i = 20;
        while (i <= 105) {
            defaultGroups.add(new SwimMastersAgeGroup(i, i + 4));
            i += 5;
        }
        return defaultGroups;
    }

    @Override
    public AgeGroup getFor(Athlete athlete) {
        int age = athlete.getAge(event.getDate());
        for (AgeGroup group : getAllOrderedByAge()) {
            if (group.containsAge(age)) {
                return group;
            }
        }
        throw new IllegalArgumentException("we have no age group for athlete with age " + age);
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public List<AgeGroup> getAllOrderedByAge() {
        ArrayList<AgeGroup> result = new ArrayList<AgeGroup>(groups);
        Collections.sort(result);
        return result;
    }
}
