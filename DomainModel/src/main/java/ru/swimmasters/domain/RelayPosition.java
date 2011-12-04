package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 12/3/11
 */
public interface RelayPosition {
    RelayTeam getRelayTeam();
    Athlete getAthlete();
    int getNumber();
}
