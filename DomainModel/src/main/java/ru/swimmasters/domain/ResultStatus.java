package ru.swimmasters.domain;

/**
 * LEN:
 *
 * This attribute is used for the result status information. An empty status
 * attribute means a regular result. The following values are allowed:
 *
 * EXH: exhibition swim (swim time = TODO: ???).
 * DSQ: athlete/relay disqualified (swim time is available).
 * DNS: athlete/relay did not start (no reason given or to late
 * withdrawal) (swim time = 0).
 * DNF: athlete/relay did not finish (swim time = TODO: ???).
 * WDR: athlete/relay was withdrawn (on time) (swim time = 0).
 *
 * SwimMasters:
 *
 * 1 | Q    (QUALIFIED, swim time is available)
 * 2 | DQ   (DSQ, swim time = 0)
 * 3 | DNS  (DNS, swim time = 0)
 * 4 | NT   (DNF, swim time = TODO: 0 or available ???)
 *
 * TODO: Stramatel?
 *
 * User: dedmajor
 * Date: 5/14/11
 */
public enum ResultStatus {
    QUALIFIED,
    DISQUALIFIED,
    DID_NOT_START,
    DID_NOT_FINISH,
    EXHIBITION,
    WITHDRAWN
}
