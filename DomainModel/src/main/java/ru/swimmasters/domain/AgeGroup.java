package ru.swimmasters.domain;

/**
 * TODO: do we really need comparable in interface?
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroup extends Comparable<AgeGroup> {
    // TODO: remove magic ints
    int NO_LOWER_BOUND = -1;
    int NO_UPPER_BOUND = -1;

    Event getEvent();

    int getMin();
    int getMax();

    boolean containsAge(int age);
}
