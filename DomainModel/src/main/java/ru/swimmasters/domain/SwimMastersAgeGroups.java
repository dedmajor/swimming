package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public abstract class SwimMastersAgeGroups implements AgeGroups {
    @Override
    public AgeGroup getFor(Athlete athlete) {
        for (AgeGroup group : getAllOrderedByAge()) {
            if (group.containsAge(athlete.getAge(getEvent().getDate()))) {
                return group;
            }
        }
        throw new IllegalArgumentException("we have no age group for athlete " + athlete);
    }
}
