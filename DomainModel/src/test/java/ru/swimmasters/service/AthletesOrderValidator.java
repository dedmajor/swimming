package ru.swimmasters.service;

import ru.swimmasters.domain.*;

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
    public void validateEntries(StartListHeats heats) {
        Heat previousHeat = null;
        for (Heat heat : heats.getAllSortedByNumber()) {
            for (Entry entry : heat.getEntries().getAllSortedByLane()) {
                if (previousHeat != null
                        && previousHeat.getYoungestAgeGroup().equals(entry.getAgeGroup())) {
                    assertThat("entry " + entry + " cannot happen after heat " + previousHeat.getNumber()
                            + " with entries " + previousHeat.getEntries().getAllSortedByLane(),
                            SwimMastersEntry.heatEntryComparator().compare(entry,
                            previousHeat.getFastestEntry(previousHeat.getYoungestAgeGroup())),
                            lessThan(0));
                }
            }
            previousHeat = heat;
        }
    }
}
