package ru.swimmasters.service;

import ru.swimmasters.domain.ApprovalStatus;
import ru.swimmasters.domain.MeetAthlete;
import ru.swimmasters.domain.SwimMastersAthlete;
import ru.swimmasters.time.Clock;

/**
 * User: dedmajor
 * Date: 5/7/11
 */
public class SwimMastersMandateCommittee implements MandateCommittee {
    private final Clock clock;

    public SwimMastersMandateCommittee(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void approveAthlete(MeetAthlete athlete) {
        setAthleteStatus(athlete, ApprovalStatus.APPROVED);
    }

    @Override
    public void rejectAthlete(MeetAthlete athlete) {
        setAthleteStatus(athlete, ApprovalStatus.REJECTED);
    }

    @Override
    public void setAthleteStatus(MeetAthlete athlete, ApprovalStatus status) {
        ((SwimMastersAthlete) athlete).setApprovalTimestamp(clock.now());
        ((SwimMastersAthlete) athlete).setApprovalStatus(status);
    }
}
