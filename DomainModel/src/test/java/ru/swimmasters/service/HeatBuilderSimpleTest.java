package ru.swimmasters.service;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import ru.swimmasters.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public class HeatBuilderSimpleTest {
    private EventEntries singleEntry;
    private EventEntries someEntries;

    @Before
    public void setUp() {
        singleEntry = singleTestEntry();
        someEntries = threeAgeGroupEntries();
    }

    @Test
    public void testNonCompetitive() {
        StartListBuilder service = new PrimitiveStartListBuilder();
        setUp();
        service.buildHeats(singleEntry);
        assertFalse("the only heat cannot be competitive", singleEntry.isAllHeatsCompetitive());
    }

    @Test
    public void testPoolLanes() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.buildHeats(someEntries);
        new PoolLanesValidator().validateEntries(someEntries);
    }

    @Test
    public void testGroupsOrder() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.buildHeats(someEntries);
        new GroupsOrderValidator().validateEntries(someEntries);

    }

    @Test
    public void testHeatNumbersOrder() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.buildHeats(someEntries);
        new HeatNumberValidator().validateEntries(someEntries);
    }

    @Test
    public void testEmptyHeatsValidator() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.buildHeats(someEntries);
        new EmptyHeatsValidator().validateEntries(someEntries);
    }

    @Test
    public void testAthletesOrder() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.buildHeats(someEntries);
        new AthletesOrderValidator().validateEntries(someEntries);
    }

    @Test
    public void testLeads() {
        PrimitiveStartListBuilder service = new PrimitiveStartListBuilder();
        service.setLeadsInAgeGroup(3);
        service.buildHeats(someEntries);
        new LeadsValidator(3).validateEntries(someEntries);
    }

    private static EventEntries threeAgeGroupEntries() {
        return new CheckedEventEntries() {
            private final SwimMastersEvent event = new SwimMastersEvent();
            {
                SwimMastersPool pool = new SwimMastersPool();
                event.setPool(pool);
                pool.setLaneMin(2);
                pool.setLaneMax(3);
            }
            private final List<Entry> entries = new ArrayList<Entry>();
            {
                event.setAgeGroups(Arrays.asList(
                        new SwimMastersAgeGroup(0, 20),
                        new SwimMastersAgeGroup(21, 25),
                        new SwimMastersAgeGroup(26, 30)
                ));
                // event.date = 2010-11-04

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
            }

            @NotNull
            @Override
            public List<Entry> getAll() {
                return entries;
            }
        };
    }

    private static EventEntries singleTestEntry() {
        return new CheckedEventEntries() {
            private final List<Entry> entries = new ArrayList<Entry>();
            {
                SwimMastersEvent event = new SwimMastersEvent();
                SwimMastersPool pool = new SwimMastersPool();
                event.setPool(pool);
                pool.setLaneMin(2);
                pool.setLaneMax(2);
                List<SwimMastersAgeGroup> ageGroups = new ArrayList<SwimMastersAgeGroup>();
                ageGroups.add(new SwimMastersAgeGroup(0, 0));
                event.setAgeGroups(ageGroups);
                // event.date = 2010-11-04
                entries.add(new SwimMastersEntry(
                        event, new SwimMastersAthlete(new LocalDate("2010-11-04")), new Duration(1)));
            }

            @NotNull
            @Override
            public List<Entry> getAll() {
                return entries;
            }
        };
    }
}
