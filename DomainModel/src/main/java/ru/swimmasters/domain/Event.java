package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface Event {
    EventGender getEventGender();
    SwimStyle getSwimStyle();
    Entries getEntries();
}
