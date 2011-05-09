package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
public interface StartListHeats {
    Event getEvent();

    List<Heat> getHeatsOrderedByNumber();

    /**
     * Check that each prepared heat is competitive.
     *
     * @see Heat#isCompetitive()
     */
    boolean isAllHeatsCompetitive();
}
