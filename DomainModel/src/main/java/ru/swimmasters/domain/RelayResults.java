package ru.swimmasters.domain;

import java.util.Map;

/**
 * User: dedmajor
 * Date: 11/30/11
 */
public interface RelayResults {
    Meet getMeet();
    RelayTeam getRelayTeam();
    Map<Event, Result> getAll();
}
