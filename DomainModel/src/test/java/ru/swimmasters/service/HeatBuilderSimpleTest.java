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
        List<Heat> heats = entries.getHeatsOrderedByNumber();
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

                SwimMastersAthlete athlete1 = new SwimMastersAthlete();
                athlete1.birthDate = new LocalDate("2010-11-04"); // 0
                SwimMastersEntry entry1 = new SwimMastersEntry(event, athlete1);

                SwimMastersAthlete athlete2 = new SwimMastersAthlete();
                athlete2.birthDate = new LocalDate("1989-11-04"); // 21
                SwimMastersEntry entry2 = new SwimMastersEntry(event, athlete2);

                SwimMastersAthlete athlete3 = new SwimMastersAthlete();
                athlete3.birthDate = new LocalDate("1980-11-04"); // 30
                SwimMastersEntry entry3 = new SwimMastersEntry(event, athlete3);

                // 1, 3, 2
                entries.add(entry1);
                entries.add(entry3);
                entries.add(entry2);
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
                entries.add(new SwimMastersEntry(new SwimMastersEvent(), new SwimMastersAthlete()));
            }

            @NotNull
            @Override
            public List<Entry> getAll() {
                return entries;
            }
        };
    }
}
