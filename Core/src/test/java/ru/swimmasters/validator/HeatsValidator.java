package ru.swimmasters.validator;

import ru.swimmasters.domain.Heat;

import java.util.List;

/**
 * User: dedmajor
 * Date: Jul 10, 2010
 */
public interface HeatsValidator {
    /**
     * Applies {@link org.junit.Assert}s to the heats
     * @param heats heats to be validated
     */
    void validate(List<Heat> heats);
}
