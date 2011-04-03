package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public abstract class SwimMastersAgeGroups implements AgeGroups {
    @Override
    public AgeGroup getFor(Athlete athlete) {
        int age = athlete.getAge(getEvent().getDate());
        for (AgeGroup group : getAllOrderedByAge()) {
            if (group.containsAge(age)) {
                return group;
            }
        }
        throw new IllegalArgumentException("we have no age group for athlete with age " + age);
    }
}
