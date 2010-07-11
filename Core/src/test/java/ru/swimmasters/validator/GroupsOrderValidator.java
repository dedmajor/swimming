package ru.swimmasters.validator;

import ru.swimmasters.domain.Application;
import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: Jul 11, 2010
 */
public class GroupsOrderValidator implements HeatsValidator {
    @Override
    public void validate(List<Heat> heats) {
        Heat previousHeat = null;
        for (Heat heat : heats) {
            for (Application application : heat.getApplications()) {
                if (previousHeat != null) {
                    assertThat(application.getAgeGroup(),
                            lessThanOrEqualTo(previousHeat.getYoungestAgeGroup()));
                }
            }
            previousHeat = heat;
        }
    }
}
