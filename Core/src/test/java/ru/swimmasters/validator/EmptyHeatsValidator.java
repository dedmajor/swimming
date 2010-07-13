package ru.swimmasters.validator;

import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: Jul 13, 2010
 */
public class EmptyHeatsValidator implements HeatsValidator {
    @Override
    public void validate(List<Heat> heats) {
        assertThat(heats.size(), greaterThan(0));
    }
}
