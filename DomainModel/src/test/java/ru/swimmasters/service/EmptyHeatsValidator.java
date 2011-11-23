package ru.swimmasters.service;

import ru.swimmasters.domain.Heat;
import ru.swimmasters.domain.StartListHeats;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public class EmptyHeatsValidator implements StartListValidator {
    @Override
    public void validateEntries(StartListHeats heats) {
        List<Heat> heatsList = heats.getAllSortedByNumber();
        assertThat(heatsList.size(), greaterThan(0));
        for (Heat heat : heatsList) {
            assertThat(heat.getEntries().getAllSortedByLane().size(), greaterThan(0));
        }
    }
}
