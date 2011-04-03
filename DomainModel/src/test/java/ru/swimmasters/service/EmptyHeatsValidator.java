package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class EmptyHeatsValidator implements StartListValidator {
    @Override
    public void validateEntries(EventEntries entries) {
        List<Heat> heats = entries.getHeatsOrderedByNumber();
        assertThat(heats.size(), greaterThan(0));
        for (Heat heat : heats) {
            assertThat(heat.getEntries().getAll().size(), greaterThan(0));
        }
    }
}
