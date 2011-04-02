package ru.swimmasters.domain;

import org.joda.time.LocalDate;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class AgeGroupTest {
    @Test
    public void testAge() {
        SwimMastersAthlete athlete = new SwimMastersAthlete();
        athlete.birthDate = new LocalDate("2000-12-31");
        assertEquals("an age is calculated on 31 of Dec",
                1, athlete.getAge(new LocalDate("2001-01-01")));
    }
}
