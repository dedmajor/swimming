package ru.swimmasters.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dedmajor
 * Date: 5/18/11
 */
public class ManualLaneResults implements LaneResults {
    private List<ManualLaneResult> results;
    private Heat heat;

    ManualLaneResults() {
        // spring mvc shall pass
    }

    public ManualLaneResults(Heat heat) {
        this.heat = heat;
        results = new ArrayList<ManualLaneResult>(heat.getEntries().getAllSortedByLane().size());
        for (Entry entry : heat.getEntries().getAllSortedByLane()) {
            results.add(new ManualLaneResult(entry));
        }
    }

    public ManualLaneResults(List<ManualLaneResult> results) {
        this.results = results;
    }

    @Override
    public Heat getHeat() {
        return heat;
    }

    @Override
    public List<ManualLaneResult> getAll() {
        return results;
    }

    public void setAll(List<ManualLaneResult> results) {
        this.results = results;
    }
}
