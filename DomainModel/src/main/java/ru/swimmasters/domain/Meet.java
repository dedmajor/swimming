package ru.swimmasters.domain;

import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
public interface Meet {
    /**
     * Numbers, letters, dash, underscore.
     */
    String getId();

    String getName();

    Pool getPool();
    Sessions getSessions();

    /**
     * @return events sorted by number
     */
    Events getEvents();

    /**
     * @return the date of the first session
     */
    LocalDate getStartDate();

    /**
     * @return the date of the last session
     */
    LocalDate getEndDate();
}
