package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/15/11
 */
public interface LaneResults {
    List<? extends FinalLaneResult> getAll();
}
