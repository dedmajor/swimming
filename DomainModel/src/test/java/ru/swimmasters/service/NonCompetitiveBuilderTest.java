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

import static org.junit.Assert.assertFalse;

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
    public void testNonCompetitive(StartListBuilder service) {
        Event singleEntryEvent = singleTestEntry();
        service.buildHeats(singleEntryEvent);
        assertFalse("the only heat cannot be competitive",
                singleEntryEvent.getEntries().isAllHeatsCompetitive());
    }

    private static Event singleTestEntry() {
        List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();
        SwimMastersEvent event = new SwimMastersEvent();
        SwimMastersPool pool = new SwimMastersPool();
        event.setPool(pool);
        pool.setLaneMin(2);
        pool.setLaneMax(2);
        event.setDate(new LocalDate("2010-11-04"));
        List<SwimMastersAgeGroup> ageGroups = new ArrayList<SwimMastersAgeGroup>();
        ageGroups.add(new SwimMastersAgeGroup(0, 0));
        event.setAgeGroups(ageGroups);
        entries.add(new SwimMastersEntry(
                event, new SwimMastersAthlete(new LocalDate("2010-11-04")), new Duration(1)));
        event.setEntries(entries);
        return event;
    }
}
