package ru.swimmasters.service;

import ru.swimmasters.domain.ApprovalStatus;
import ru.swimmasters.domain.MeetAthlete;
import ru.swimmasters.domain.SwimMastersMeetAthlete;
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
        ((SwimMastersMeetAthlete) athlete).setApprovalTimestamp(clock.now());
        ((SwimMastersMeetAthlete) athlete).setApprovalStatus(status);
    }
}
