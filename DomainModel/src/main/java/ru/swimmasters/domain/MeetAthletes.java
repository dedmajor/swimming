package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * User: dedmajor
 * Date: 8/21/11
 */
public interface MeetAthletes {
    Meet getMeet();
    Collection<MeetAthlete> getAll();
    List<MeetAthlete> getAllSortedByAthleteName();
    @Nullable
    MeetAthlete getByAthlete(Athlete athlete);
}
