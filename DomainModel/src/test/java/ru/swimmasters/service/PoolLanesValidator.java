package ru.swimmasters.service;

import ru.swimmasters.domain.*;

import java.util.Arrays;

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
    public void validateEntries(StartListHeats heats) {
        Pool pool = heats.getEvent().getPool();
        boolean[] busyLane = new boolean[pool.getLaneMax()];
        for (Heat heat : heats.getAllSortedByNumber()) {
            Arrays.fill(busyLane, false);
            for (Entry entry : heat.getEntries().getAllSortedByLane()) {
                Integer lane = entry.getLane();
                assertNotNull("lane must be set", lane);
                assertTrue("heats must be only on the allowed meet lanes, but given: " + lane,
                        pool.isMeetLane(lane));
                assertFalse("lane " + lane + " must be free", busyLane[lane - 1]);
                busyLane[lane - 1] = true;
            }
        }
    }
}
