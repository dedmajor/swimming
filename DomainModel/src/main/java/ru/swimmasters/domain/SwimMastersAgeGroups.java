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

    /**
     * В индивидуальных номерах программы участники выступают в следующих возрастных
     * категориях (принадлежность к возрастной категории определяется по состоянию на
     * 31 декабря):
     * <p/>
     * 25-29 1986-1982 гг.р
     * 50-54 1961-1957 гг.р.
     * 75-79 1936-1932 гг.р.
     * 30-34 1981-1977 гг.р.
     * 55-59 1956-1952 гг.р.
     * 80-84 1931-1927 гг.р.
     * 35-39 1976-1972 гг.р.
     * 60-64 1951-1947 гг.р.
     * 85-89 1926-1922 гг.р.
     * 40-44 1971-1967 гг.р.
     * 65-69 1946-1942 гг.р.
     * 90-94 1921-1917 гг.р
     * 45-49 1966-1962 гг.р.
     * 70-74 1941-1937 гг.р.
     * 95+ 1916 г.р. и старше
     * <p/>
     * В эстафетах команды выступают в следующих возрастных категориях:
     * <p/>
     * 100-119 лет; 120-159 лет; 160-199 лет; 200-239 лет;
     * 240-279 лет; 280-319 лет; 320-359 лет
     */
    public static List<SwimMastersAgeGroup> createDefaultGroups() {
        List<SwimMastersAgeGroup> defaultGroups = new ArrayList<SwimMastersAgeGroup>();
        //defaultGroups.add(new SwimMastersAgeGroup(AgeGroup.NO_LOWER_BOUND, 19));
        int i = 25;
        while (i <= 90) {
            defaultGroups.add(new SwimMastersAgeGroup(i, i + 4));
            i += 5;
        }
        defaultGroups.add(new SwimMastersAgeGroup(95, AgeGroup.NO_UPPER_BOUND));
        // TODO: FIXME: relays?
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
        throw new IllegalArgumentException(
                "we have no age group for athlete with age " + age
                        + " which is born at " + athlete.getBirthYear());
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
