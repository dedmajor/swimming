package ru.swimmasters.service;

import ru.swimmasters.domain.*;
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
        if (status == ApprovalStatus.APPROVED) {
            validateAthleteAge(athlete);
        }
        ((SwimMastersMeetAthlete) athlete).setApprovalTimestamp(clock.now());
        ((SwimMastersMeetAthlete) athlete).setApprovalStatus(status);
    }

    private static void validateAthleteAge(MeetAthlete athlete) {
        for (Entry entry : athlete.getEntries().getAll()) {
            // TODO: extract to interface?
            if (!((SwimMastersEntry) entry).isValidAge()) {
                throw new IllegalArgumentException("athlete " + athlete
                        + " cannot participate in event " + entry.getEvent());
            }
        }
    }
}
