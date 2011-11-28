package ru.swimmasters.domain;

import java.util.List;

/**
 * TODO: rename to heat results
 *
 * User: dedmajor
 * Date: 5/15/11
 */
public interface LaneResults {
    Heat getHeat();
    List<? extends FinalLaneResult> getAll();
}
