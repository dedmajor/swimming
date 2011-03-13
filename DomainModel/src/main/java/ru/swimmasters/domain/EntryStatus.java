package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 3/13/11
 */
public enum EntryStatus {
    REGULAR,
    //EXHIBITION, // TODO: LEN statuses
    REJECTED,
    //WITHDRAWN, // TODO: LEN statuses, constraint: entry have no heat or lane?
}
