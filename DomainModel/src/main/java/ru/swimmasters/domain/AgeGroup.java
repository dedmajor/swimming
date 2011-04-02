package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public interface AgeGroup extends Comparable<AgeGroup> {
    int getMin();
    int getMax();

    boolean containsAge(int age);
}
