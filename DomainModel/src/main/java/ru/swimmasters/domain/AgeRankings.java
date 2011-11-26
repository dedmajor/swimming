package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
public interface AgeRankings {
    // TODO: getEvent() to clarify contract on all containing elements?
    // TODO: either sorted by list or collection?
    List<AgeRanking> getAll();
}
