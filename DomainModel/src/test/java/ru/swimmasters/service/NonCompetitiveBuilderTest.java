package ru.swimmasters.service;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import ru.swimmasters.domain.*;

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
    public static final StartListBuilder SWIM_MASTERS_SERVICE = new SwimMastersStartListBuilder();

    @Theory
    public void testNonCompetitive(StartListBuilder service) {
        EventEntries singleEntry = singleTestEntry();
        service.buildHeats(singleEntry);
        assertFalse("the only heat cannot be competitive", singleEntry.isAllHeatsCompetitive());
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
