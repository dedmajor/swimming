package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 8/27/11
 */
public interface AgeRanking {
    Event getEvent();

    /**
     * @return age group from the event age groups collections
     * @see Event#getAgeGroups()
     */
    AgeGroup getAgeGroup();
    GroupRankings getGroupRankings();
}
