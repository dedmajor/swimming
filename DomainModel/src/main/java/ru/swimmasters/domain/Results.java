package ru.swimmasters.domain;

import java.util.Map;

/**
 * SwimRankings:
 * - Full results, including splits and places are available
 * - All results (prelims,finals) are available, but splits and/or places are missing.
 * - Best result of every swimmer in every event is available.
 * - Partial results of the meet are available.
 * - All results of the meet are missing.
 *
 * User: dedmajor
 * Date: 5/15/11
 */
public interface Results {
    Map<Event, Result> getAll();
}
