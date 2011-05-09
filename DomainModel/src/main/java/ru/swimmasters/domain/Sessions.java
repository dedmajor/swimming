package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
public interface Sessions {
    List<Session> getAll();

    @Nullable
    Session findByDate(LocalDate date);
}
