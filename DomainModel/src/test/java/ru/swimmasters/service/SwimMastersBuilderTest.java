package ru.swimmasters.service;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.RealTimeClock;

import java.util.Arrays;

/**
 * Heat builder which MUST keep leaders (the fastest athletes) of the same age group together
 * in the best heat (i. e. the last heat, heat with the maximum number) of each group.
 *
 * Younger groups MUST swim later in time (i. e. in greater heat numbers).
 *
 * Faster sportsmen MUST swim later in each group (i. e. in the greater heat number).
 *
 * Several groups CAN swim together in the same heat.
 *
 * User: dedmajor
 * Date: 4/3/11
 */
@RunWith(Theories.class)
public class SwimMastersBuilderTest {
    private static final SwimMastersStartListBuilder SWIM_MASTERS_SERVICE
            = new SwimMastersStartListBuilder(new RealTimeClock());
    static {
        SWIM_MASTERS_SERVICE.setLeadsInAgeGroup(3);
    }

    @DataPoints
    public static final SwimMastersEvent[] EVENTS = new SwimMastersEvent[2];
    static {
        SwimMastersEvent event1 = createEvent();
        EVENTS[0] = event1;
        EVENTS[0].setEntries(Arrays.asList(
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(25000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(24500L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(27000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(24000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1978-11-04")),
                        new Duration(24000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1979-11-04"), "Z", null),
                        new Duration(24000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1979-11-04"), "C", null),
                        new Duration(24000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1979-11-04"), "A", null),
                        new Duration(24000L)),
                new SwimMastersEntry(event1,
                        new SwimMastersAthlete(new LocalDate("1979-11-04"), "B", null),
                        new Duration(24000L))
        ));

        SwimMastersEvent event2 = createEvent();
        EVENTS[1] = event2;
        EVENTS[1].setEntries(Arrays.asList(
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(25000L)),
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(27000L)),
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1979-11-04")),
                        new Duration(24000L)),
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1985-11-04")),
                        new Duration(25500L)),
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1984-11-04")),
                        new Duration(24500L)),
                new SwimMastersEntry(event2,
                        new SwimMastersAthlete(new LocalDate("1984-11-04")),
                        new Duration(28000L))
        ));

        for (Event event : EVENTS) {
            for (Entry entry : event.getEntries().getAll()) {
                ((SwimMastersAthlete) entry.getAthlete()).setApprovalStatus(ApprovalStatus.APPROVED);
            }
        }
    }

    private static SwimMastersEvent createEvent() {
        SwimMastersEvent event = new SwimMastersEvent();
        SwimMastersPool pool = new SwimMastersPool();
        pool.setLaneMin(2);
        pool.setLaneMax(6);
        event.setPool(pool);
        event.setAgeGroups(SwimMastersAgeGroups.createDefaultGroups());
        event.setDate(new LocalDate(2010, 04, 25));
        return event;
    }

    @Theory
    public void testEntries(Event event) {
        SWIM_MASTERS_SERVICE.buildHeats(event);

        new EmptyHeatsValidator().validateEntries(event.getEntries());
        new HeatNumberValidator().validateEntries(event.getEntries());
        new PoolLanesValidator().validateEntries(event.getEntries());
        new GroupsOrderValidator().validateEntries(event.getEntries());
        new AthletesOrderValidator().validateEntries(event.getEntries());
        new LeadsValidator(3).validateEntries(event.getEntries());
    }
}
