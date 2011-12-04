package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 12/3/11
 */
public interface RelayPositions {
    RelayTeam getRelayTeam();
    List<RelayPosition> getAll();
}
