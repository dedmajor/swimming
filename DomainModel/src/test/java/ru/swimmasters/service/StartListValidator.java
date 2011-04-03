package ru.swimmasters.service;

import ru.swimmasters.domain.EventEntries;
import ru.swimmasters.domain.Heat;

import java.util.List;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
public interface StartListValidator {
    void validateEntries(EventEntries entries);
}
