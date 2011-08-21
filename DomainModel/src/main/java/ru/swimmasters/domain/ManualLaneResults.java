package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/18/11
 */
public class ManualLaneResults implements LaneResults {
    private List<ManualLaneResult> results;

    ManualLaneResults() {
        // spring mvc shall pass
    }

    public ManualLaneResults(List<ManualLaneResult> results) {
        this.results = results;
    }

    @Override
    public List<ManualLaneResult> getAll() {
        return results;
    }

    public void setAll(List<ManualLaneResult> results) {
        this.results = results;
    }
}
