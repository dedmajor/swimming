package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Entries {
    @NotNull
    List<Entry> getAll();
}
