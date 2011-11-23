package ru.swimmasters.service;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.RealTimeClock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
@RunWith(Theories.class)
public class NonCompetitiveBuilderTest {
    @DataPoint
    public static final StartListBuilder PRIMITIVE_SERVICE = new PrimitiveStartListBuilder();
    @DataPoint
    public static final StartListBuilder SWIM_MASTERS_SERVICE = new SwimMastersStartListBuilder(new RealTimeClock());

    @Theory
    public void testReturnValue(StartListBuilder service) {
        Event event = singleTestEntry();
        List<Heat> initialResult = service.buildHeats(event);
        assertTrue("initial result must be empty", initialResult.isEmpty());
        List<Heat> previous = service.buildHeats(event);
        assertThat("there should be previous heats", previous.size(), greaterThan(0));
    }

    @Theory
    public void testNonCompetitive(StartListBuilder service) {
        Event singleEntryEvent = singleTestEntry();
        service.buildHeats(singleEntryEvent);
        assertEquals("there should be the only heat",
                1, singleEntryEvent.getStartListHeats().getAllSortedByNumber().size());
        assertFalse("the only heat cannot be competitive",
                singleEntryEvent.getStartListHeats().isAllHeatsCompetitive());
    }

    private static Event singleTestEntry() {
        List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();
        SwimMastersPool pool = new SwimMastersPool(2, 2);
        SwimMastersMeet meet = new SwimMastersMeet(pool);
        SwimMastersSession session = new SwimMastersSession(meet, new LocalDate("2010-11-04"));
        SwimMastersEvent event = new SwimMastersEvent(session);
        List<SwimMastersAgeGroup> ageGroups = new ArrayList<SwimMastersAgeGroup>();
        ageGroups.add(new SwimMastersAgeGroup(0, 0));
        event.setAgeGroups(ageGroups);
        SwimMastersAthlete athlete = new SwimMastersAthlete(new LocalDate("2010-11-04"));
        SwimMastersMeetAthlete meetAthlete = meet.addAthlete(athlete);
        meetAthlete.setApprovalStatus(ApprovalStatus.APPROVED);
        entries.add(new SwimMastersEntry(
                event, meetAthlete, new Duration(1)));
        event.setEntries(entries);
        return event;
    }
}
