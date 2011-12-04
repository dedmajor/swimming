package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 11/30/11
 */
public interface RelayTeam {
    Meet getMeet();
    String getName();

    RelayPositions getRelayPositions();
    int getSize();

    RelayEntries getRelayEntries();
    RelayResults getRelayResults();
}
