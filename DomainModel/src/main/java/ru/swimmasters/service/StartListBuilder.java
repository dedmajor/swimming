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
     *
     * Event MUST have at least one entry, otherwise IllegalArgumentException is thrown.
     *
     * TODO: not confuse the previous heats with return value, use method of the event
     *
     * @return previous heats of the event (useful for cleanup)
     */
    List<Heat> buildHeats(Event event);

    /**
     * Builds all heats for all events and sets an absolute number on each heat.
     *
     * @return previous heats of the event (useful for cleanup)
     */
    List<Heat> prepareHeats(Meet meet);
}
