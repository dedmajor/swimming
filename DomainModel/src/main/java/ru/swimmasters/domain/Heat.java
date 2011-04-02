package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Heat {
    int getNumber();
    EventEntries getEntries();

    /**
     * @return true if there is more than one entry, false otherwise
     */
    boolean isCompetitive();
}
