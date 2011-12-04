package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

/**
 * User: dedmajor
 * Date: 11/30/11
 */
public interface RelayEntries {
    Meet getMeet();
    Club getClub();
    RelayTeam getRelayTeam();
    @NotNull
    Iterable<Entry> getAll();
}
