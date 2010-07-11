package ru.swimmasters.validator;

import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * User: dedmajor
 * Date: Jul 10, 2010
 */
public class SingleAthleteValidator implements HeatsValidator {
    @Override
    public void validate(List<Heat> heats) {
        for (Heat heat : heats) {
            assertTrue("all heats MUST contain at least two athletes", heat.isCompetitive());
        }
    }
}
