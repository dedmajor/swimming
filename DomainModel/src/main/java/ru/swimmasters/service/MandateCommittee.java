package ru.swimmasters.service;

import ru.swimmasters.domain.ApprovalStatus;
import ru.swimmasters.domain.MeetAthlete;

/**
 * User: dedmajor
 * Date: 5/7/11
 */
public interface MandateCommittee {
    void approveAthlete(MeetAthlete athlete);
    void rejectAthlete(MeetAthlete athlete);
    void setAthleteStatus(MeetAthlete athlete, ApprovalStatus status);
}
