package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import static junit.framework.Assert.assertTrue;

/**
 * User: dedmajor
 * Date: Jul 10, 2010
 */
public class SingleAthleteValidator implements StartListValidator {
    @Override
    public void validateEntries(EventEntries entries) {
        for (Heat heat : entries.getHeatsOrderedByNumber()) {
            assertTrue("all heats MUST contain at least two athletes", heat.isCompetitive());
        }
    }
}
