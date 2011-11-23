package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 11/20/11
 */
public interface HeatEntries {
    Heat getHeat();
    List<Entry> getAllSortedByLane();
}
