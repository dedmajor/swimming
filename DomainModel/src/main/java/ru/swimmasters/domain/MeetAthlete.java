package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 * An individual athlete in a context of a single meet.
 *
 * TODO: remove getAthlete().getAthlete() sematics
 * rename to Athlete or Individual (in a contrast to RelayTeam), use getAthlete().getSportsperson()
 *
 * User: dedmajor
 * Date: 4/2/11
 *
 * @see RelayTeam
 */
public interface MeetAthlete {
    Athlete getAthlete();
    Meet getMeet();
    // TODO: getIndividualEntries() ?
    AthleteEntries getEntries();

    // TODO: extract object Approval
    @NotNull
    ApprovalStatus getApprovalStatus();
    @Nullable
    DateTime getApprovalTimestamp();

    Results getResults();
}
