package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 * An athlete in a context of a single meet.
 *
 * TODO: remove getAthlete().getAthlete() sematics
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface MeetAthlete {
    Athlete getAthlete();
    Meet getMeet();
    AthleteEntries getEntries();

    @NotNull
    ApprovalStatus getApprovalStatus();
    @Nullable
    DateTime getApprovalTimestamp();

    Results getResults();
}
