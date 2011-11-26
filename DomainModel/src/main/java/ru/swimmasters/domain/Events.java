package ru.swimmasters.domain;

import java.util.List;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
public interface Events {
    // TODO: either collection or sorting criteria (by number)?
    List<Event> getAll();
}
