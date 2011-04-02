package ru.swimmasters.service;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import ru.swimmasters.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public class HeatBuilderTest {
    @Test
    public void testNonCompetitive() {
        HeatBuilderService service = new SwimMastersHeatBuilder();
        EventEntries entries = singleTestEntry();
        service.buildHeats(entries);
        assertFalse("the only heat cannot be competitive", entries.isAllHeatsCompetitive());
    }

    private static EventEntries singleTestEntry() {
        return new CheckedEventEntries() {
            private final List<Entry> entries = new ArrayList<Entry>();
            {
                SwimMastersEntry entry = new SwimMastersEntry();
                entry.athlete = new SwimMastersAthlete();
                entry.event = new SwimMastersEvent();
                entries.add(entry);
            }

            @NotNull
            @Override
            public List<Entry> getAll() {
                return entries;
            }
        };
    }
}
