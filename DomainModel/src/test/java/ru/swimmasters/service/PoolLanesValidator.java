package ru.swimmasters.service;

import ru.swimmasters.domain.Entry;
import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Validates that lane numbers match pool capacity and no one swims
 * on the same lane in one heat.
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public class PoolLanesValidator implements StartListValidator {
    @Override
    public void validateEntries(EventEntries entries) {
        List<Heat> heats = entries.getHeatsOrderedByNumber();
        boolean[] busyLane = new boolean[entries.getEvent().getPool().getLaneMax()];
        for (Heat heat : heats) {
            Arrays.fill(busyLane, false);
            for (Entry entry : heat.getEntries().getAll()) {
                Integer lane = entry.getLane();
                assertNotNull("lane must be set", lane);
                assertTrue("heats must be only on the allowed meet lanes, but given: " + lane,
                        entries.getEvent().getPool().isMeetLane(lane));
                assertFalse("lane " + lane + " must be free", busyLane[lane - 1]);
                busyLane[lane - 1] = true;
            }
        }
    }
}