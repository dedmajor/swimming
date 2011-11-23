package ru.swimmasters.service;

import ru.swimmasters.domain.Heat;
import ru.swimmasters.domain.StartListHeats;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Asserts that younger groups swim later in time.
 *
 * User: dedmajor
 * Date: Jul 11, 2010
 */
public class GroupsOrderValidator implements StartListValidator {
    @Override
    public void validateEntries(StartListHeats heats) {
        Heat previousHeat = null;
        for (Heat heat : heats.getAllSortedByNumber()) {
            if (previousHeat != null) {
                assertThat("heat number " + heat.getNumber() + " must have younger groups",
                        heat.getOldestAgeGroup(),
                        lessThanOrEqualTo(previousHeat.getYoungestAgeGroup()));
            }
            previousHeat = heat;
        }
    }
}
