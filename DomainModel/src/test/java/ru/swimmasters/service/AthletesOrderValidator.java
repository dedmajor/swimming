package ru.swimmasters.service;

import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;
import ru.swimmasters.domain.SwimMastersEntry;

import java.util.List;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * Asserts that faster sportsmen swim later in each group.
 *
 * If two entries have the same entry time, order is taken from
 * {@link SwimMastersEntry.HeatEntryComparator}.
 *
 * User: dedmajor
 * Date: Jul 11, 2010
 */
public class AthletesOrderValidator implements StartListValidator {
    @Override
    public void validateEntries(EventEntries entries) {
        List<Heat> heats = entries.getHeatsOrderedByNumber();
        Heat previousHeat = null;
        for (Heat heat : heats) {
            for (Entry entry : heat.getEntries().getAll()) {
                if (previousHeat != null
                        && previousHeat.getYoungestAgeGroup().equals(entry.getAgeGroup())) {
                    assertThat(SwimMastersEntry.heatEntryComparator().compare(entry,
                            previousHeat.getFastestEntry(previousHeat.getYoungestAgeGroup())),
                            lessThan(0));
                }
            }
            previousHeat = heat;
        }
    }
}
