package ru.swimmasters.service;

import ru.swimmasters.domain.StartListHeats;

/**
 * TODO: throw checked exception instead of JUnit dependency and move into domain
 *
 * User: dedmajor
 * Date: 4/3/11
 */
public interface StartListValidator {
    void validateEntries(StartListHeats heats);
}
