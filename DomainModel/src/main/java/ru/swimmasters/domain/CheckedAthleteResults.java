package ru.swimmasters.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation or Results interface which ensures
 * that all results belong to the same athlete.
 *
 * User: dedmajor
 * Date: 5/15/11
 */
public class CheckedAthleteResults implements Results {
    private final List<? extends Result> results;

    public CheckedAthleteResults(List<? extends Result> results) {
        this.results = results;
        checkSameAthlete(results);
    }

    private static void checkSameAthlete(List<? extends Result> athleteResults) {
        Athlete athlete = null;
        for (Result athleteResult : athleteResults) {
            if (athlete == null) {
                athlete = athleteResult.getAthlete().getAthlete();
            } else if (!athleteResult.getAthlete().equals(athlete)) {
                throw new IllegalArgumentException("all results must be of the same athlete");
            }
        }
    }

    @Override
    public Map<Event, Result> getAll() {
        Map<Event,Result> result = new HashMap<Event, Result>(results.size());
        for (Result athleteResult : results) {
            if (result.get(athleteResult.getEvent()) != null) {
                throw new IllegalStateException("results cannot contain more that entry for each event");
            }
            result.put(athleteResult.getEvent(), athleteResult);
        }
        return result;
    }
}
