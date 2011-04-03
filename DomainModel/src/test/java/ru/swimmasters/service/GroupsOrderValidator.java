package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Asserts that younger groups swim later in time.
 *
 * User: dedmajor
 * Date: Jul 11, 2010
 */
public class GroupsOrderValidator implements StartListValidator {
    @Override
    public void validateEntries(EventEntries entries) {
        List<Heat> heats = entries.getHeatsOrderedByNumber();
        Heat previousHeat = null;
        for (Heat heat : heats) {
            if (previousHeat != null) {
                assertThat("heat number " + heat.getNumber() + " must gave younger groups",
                        heat.getOldestAgeGroup(),
                        lessThanOrEqualTo(previousHeat.getYoungestAgeGroup()));
            }
            previousHeat = heat;
        }
    }
}
