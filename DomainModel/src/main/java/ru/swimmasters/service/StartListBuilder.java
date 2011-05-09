package ru.swimmasters.service;

import ru.swimmasters.domain.*;

import java.util.List;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
public interface StartListBuilder {
    /**
     * Prepares and sets {@link Heat} and lane information for each {@link Entry}
     * with the regular status.
     * Event MUST have at least one entry, otherwise IllegalArgumentException is thrown.
     *
     * TODO: rename to prepareHeats()?
     *
     * @return previous heats of the event (useful for cleanup)
     */
    List<Heat> buildHeats(Event event);
}
