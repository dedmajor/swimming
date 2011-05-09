package ru.swimmasters.service;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import ru.swimmasters.domain.*;
import ru.swimmasters.time.RealTimeClock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
@RunWith(Theories.class)
public class SimpleBuildersTest {
    private static final StartListBuilder PRIMITIVE_SERVICE = new PrimitiveStartListBuilder();
    private static final StartListBuilder SWIM_MASTERS_SERVICE
            = new SwimMastersStartListBuilder(new RealTimeClock());

    @DataPoints
    public static final BuilderEntries[] DATA_POINTS = {
            new BuilderEntries(PRIMITIVE_SERVICE, threeAgeGroupEntries()),
            new BuilderEntries(SWIM_MASTERS_SERVICE, threeAgeGroupEntries()),
    };

    @Theory
    public void testPoolLanes(BuilderEntries serviceEntries) {
        serviceEntries.service.buildHeats(serviceEntries.event);
        new PoolLanesValidator().validateEntries(serviceEntries.event.getStartListHeats());
    }

    @Theory
    public void testGroupsOrder(BuilderEntries serviceEntries) {
        serviceEntries.service.buildHeats(serviceEntries.event);
        new GroupsOrderValidator().validateEntries(serviceEntries.event.getStartListHeats());

    }

    @Theory
    public void testHeatNumbersOrder(BuilderEntries serviceEntries) {
        serviceEntries.service.buildHeats(serviceEntries.event);
        new HeatNumberValidator().validateEntries(serviceEntries.event.getStartListHeats());
    }

    @Theory
    public void testEmptyHeatsValidator(BuilderEntries serviceEntries) {
        serviceEntries.service.buildHeats(serviceEntries.event);
        new EmptyHeatsValidator().validateEntries(serviceEntries.event.getStartListHeats());
    }

    @Theory
    public void testAthletesOrder(BuilderEntries serviceEntries) {
        serviceEntries.service.buildHeats(serviceEntries.event);
        new AthletesOrderValidator().validateEntries(serviceEntries.event.getStartListHeats());
    }

    private static class BuilderEntries {
        public final StartListBuilder service;
        public final Event event;

        private BuilderEntries(StartListBuilder service, Event event) {
            this.service = service;
            this.event = event;
            for (Entry entry : event.getEntries().getAll()) {
                ((SwimMastersAthlete) entry.getAthlete()).setApprovalStatus(ApprovalStatus.APPROVED);
            }
        }
    }

    private static Event threeAgeGroupEntries() {
        SwimMastersEvent event = new SwimMastersEvent();
        SwimMastersPool pool = new SwimMastersPool();
        event.setPool(pool);
        pool.setLaneMin(2);
        pool.setLaneMax(3);
        event.setDate(new LocalDate("2010-11-04"));

        List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();
        event.setAgeGroups(Arrays.asList(
                new SwimMastersAgeGroup(0, 20),
                new SwimMastersAgeGroup(21, 25),
                new SwimMastersAgeGroup(26, 30)
        ));

        entries.add(
                new SwimMastersEntry(event,
                        new SwimMastersAthlete(new LocalDate("2010-11-04")),
                        new Duration(1L))); // 0

        entries.add(
                new SwimMastersEntry(event,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(1L))); // 30 - 1 sec
        entries.add(
                new SwimMastersEntry(event,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(3L))); // 30 - 3 sec
        entries.add(
                new SwimMastersEntry(event,
                        new SwimMastersAthlete(new LocalDate("1980-11-04")),
                        new Duration(2L))); // 30 - 2 sec

        entries.add(
                new SwimMastersEntry(event, new SwimMastersAthlete(new
                        LocalDate("1989-11-04")),
                        new Duration(1L))); // 21

        event.setEntries(entries);

        for (Entry entry : event.getEntries().getAll()) {
            ((SwimMastersAthlete) entry.getAthlete()).setApprovalStatus(ApprovalStatus.APPROVED);
        }

        return event;
    }
}
