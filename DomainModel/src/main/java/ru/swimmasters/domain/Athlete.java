package ru.swimmasters.domain;

import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 3/12/11
 */
public interface Athlete {
    Long getId();
    LocalDate getBirthDate();
    String getFirstName();
    String getLastName();
    String getFullName();

    int getBirthYear();
    int getAge(LocalDate date);

    Club getClub();
}
