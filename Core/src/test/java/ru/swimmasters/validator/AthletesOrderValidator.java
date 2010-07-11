package ru.swimmasters.validator;

import ru.swimmasters.domain.Application;
import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: Jul 11, 2010
 */
public class AthletesOrderValidator implements HeatsValidator {
    @Override
    public void validate(List<Heat> heats) {
        Heat previousHeat = null;
        for (Heat heat : heats) {
            for (Application application : heat.getApplications()) {
                if (previousHeat != null
                        && previousHeat.getYoungestAgeGroup() == application.getAgeGroup()) {
                    assertThat(application,
                            lessThan(previousHeat.getFastestApplication(application.getAgeGroup())));
                }
            }
            previousHeat = heat;
        }
    }
}
