package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

/**
 * An athlete in a context of a single meet.
 *
 * User: dedmajor
 * Date: 4/2/11
 */
public interface MeetAthlete extends Athlete {
    // Meet getMeet();
    Entries getEntries();

    @Nullable
    ApprovalStatus getApprovalStatus();
    @Nullable
    DateTime getApprovalTimestamp();
}
