package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;
import ru.swimmasters.domain.StartListHeats;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Checks that all heats are numbered in sequence 1, 2, 3, ...
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public class HeatNumberValidator implements StartListValidator {
    @Override
    public void validateEntries(StartListHeats heats) {
        List<Heat> heatsList = heats.getHeatsOrderedByNumber();
        for (int i = 0; i < heatsList.size(); i++) {
            assertEquals("heat numbers must be a sequence",
                    i + 1, heatsList.get(i).getNumber());
        }
    }
}
