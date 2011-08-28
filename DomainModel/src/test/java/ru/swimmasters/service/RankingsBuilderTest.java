package ru.swimmasters.service;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.Test;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.RealTimeClock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
public class RankingsBuilderTest {
    private final RankingsBuilder builder = new SwimMastersRankingsBuilder(new RealTimeClock());

    @Test
    public void smokeTest() {
        SwimMastersMeet meet = new SwimMastersMeet(new SwimMastersPool(1, 8));
        SwimMastersSession session = new SwimMastersSession(meet, new LocalDate("2011-08-01"));
        SwimMastersEvent event = new SwimMastersEvent(session);
        event.setAgeGroups(SwimMastersAgeGroups.createDefaultGroups());
        SwimMastersAthlete athlete = new SwimMastersAthlete(new LocalDate("1984-10-19"));
        SwimMastersEntry entry = new SwimMastersEntry(event,
                new SwimMastersMeetAthlete(meet, athlete), Duration.ZERO);
        event.addEntry(entry);

        entry.setResult(new SwimMastersResult(entry));

        AgeRankings ageRankings = builder.buildEventAgeRankings(event);

        assertEquals("age rankings should be set", 1, ageRankings.getAll().size());

        GroupRankings groupRankings = ageRankings.getAll().get(0).getGroupRankings();
        assertEquals("the only group should be present", 1, groupRankings.getAll().size());
        Integer place = groupRankings.getAll().get(0).getPlace();
        assertNotNull("place should not be null", place);
        assert place != null;
        assertEquals("the only athlete should be at the first place",
                1, (int) place);
    }
}
