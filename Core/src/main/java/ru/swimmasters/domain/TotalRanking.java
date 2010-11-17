package ru.swimmasters.domain;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * TODO: maps applications to results for particular event
 * 
 * User: dedmajor
 * Date: Nov 13, 2010
 */
public class TotalRanking {
    private final Event event;
    private final SortedSet<Result> absoluteResults = new TreeSet<Result>();

    public TotalRanking(Event event) {
        this.event = event;
    }
    
    public void addResult(Result result) {
        if (!event.containsApplication(result.getAppliaction())) {
            throw new IllegalArgumentException("event " + event + " doesn't contains result application "
                    + result.getAppliaction());
        }
        absoluteResults.add(result);
    }

    public SortedSet<Result> getAbsoluteResults() {
        return absoluteResults;
    }
}
