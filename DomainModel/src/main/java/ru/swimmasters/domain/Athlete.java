package ru.swimmasters.domain;

import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 3/12/11
 */
public interface Athlete {
    LocalDate getBirthDate();
    String getFirstName();
    String getLastName();
}
