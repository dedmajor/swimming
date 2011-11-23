package ru.swimmasters.service;

import ru.swimmasters.domain.Heat;
import ru.swimmasters.domain.StartListHeats;

import static junit.framework.Assert.assertTrue;

/**
 * User: dedmajor
 * Date: Jul 10, 2010
 */
public class SingleAthleteValidator implements StartListValidator {
    @Override
    public void validateEntries(StartListHeats heats) {
        for (Heat heat : heats.getAllSortedByNumber()) {
            assertTrue("all heats MUST contain at least two athletes", heat.isCompetitive());
        }
    }
}
