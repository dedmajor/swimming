package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.List;

/**
 * TODO: throw checked exception instead of JUnit dependency and move into domain
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public interface StartListValidator {
    void validateEntries(EventEntries entries);
}
