package ru.swimmasters.parser;

import ru.swimmasters.domain.Discipline;

/**
 * User: dedmajor
 * Date: Nov 21, 2010
 */
public interface DisciplineParser {
    Discipline parseDiscipline(String textContent);
    boolean isDiscipline(String textContent);
}
