package ru.swimmasters.domain;

import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
public interface Session {
    Meet getMeet();
    LocalDate getDate();
    Events getEvents();

    /**
     * @return session number unique within the meet
     */
    int getNumber();
}
