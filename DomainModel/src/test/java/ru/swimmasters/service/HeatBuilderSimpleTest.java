package ru.swimmasters.service;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
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
    @Test
    public void testNonCompetitive() {
        HeatBuilderService service = new SwimMastersHeatBuilder();
        EventEntries entries = singleTestEntry();
        service.buildHeats(entries);
        assertFalse("the only heat cannot be competitive", entries.isAllHeatsCompetitive());
    }

    @Test
    public void testPoolLanes() {
        SwimMastersHeatBuilder service = new SwimMastersHeatBuilder();
        EventEntries entries = threeAgeGroupEntries();
        service.buildHeats(entries);
        new PoolLanesValidator().validateEntries(entries);
    }

    @Test
    public void testGroupsOrder() {
        SwimMastersHeatBuilder service = new SwimMastersHeatBuilder();
        EventEntries entries = threeAgeGroupEntries();
        service.buildHeats(entries);
        new GroupsOrderValidator().validateEntries(entries);
    }

    private static EventEntries threeAgeGroupEntries() {
        return new CheckedEventEntries() {
            private final SwimMastersEvent event = new SwimMastersEvent();
            {
                SwimMastersPool pool = new SwimMastersPool();
                event.setPool(pool);
                pool.setLaneMin(2);
                pool.setLaneMax(2);
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
                        new SwimMastersEntry(event, new SwimMastersAthlete(new LocalDate("2010-11-04")))); // 0
                entries.add(
                        new SwimMastersEntry(event, new SwimMastersAthlete(new LocalDate("1980-11-04")))); // 30
                entries.add(
                        new SwimMastersEntry(event, new SwimMastersAthlete(new LocalDate("1989-11-04")))); // 21
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
                ArrayList<SwimMastersAgeGroup> ageGroups = new ArrayList<SwimMastersAgeGroup>();
                ageGroups.add(new SwimMastersAgeGroup(0, 0));
                event.setAgeGroups(ageGroups);
                // event.date = 2010-11-04
                entries.add(new SwimMastersEntry(
                        event, new SwimMastersAthlete(new LocalDate("2010-11-04"))));
            }

            @NotNull
            @Override
            public List<Entry> getAll() {
                return entries;
            }
        };
    }
}
