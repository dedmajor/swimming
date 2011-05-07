package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroup extends Comparable<AgeGroup> {
    int NO_LOWER_BOUND = -1;
    int NO_UPPER_BOUND = -1;

    int getMin();
    int getMax();

    boolean containsAge(int age);
}
