package ru.swimmasters.domain;


/**
 * This element contains the information for a single entry of an athlete or a relay
 * to a specific round of a meet.
 *
 * TODO: The combination of the attributes eventid, heatid and lane should be unique
 * over all ENTRY objects of the same meet.
 *
 * User: dedmajor
 * Date: 3/13/11
 */
public class SwimmastersEntry {
    // LEN (9 fields)
    String entryTime; // TODO: FIXME: SwimTime or LocalTime? SQL: interval?

    /**
     * Reference to the EVENT element using the id attribute.
     * Required field.
     */
    Integer eventId;
    Integer heatId;
    Integer lane;

    /**
     * This attribute is used for the entry status information.
     */
    EntryStatus status = EntryStatus.REGULAR;

    //Integer ageGroupId;
    //Integer entryCourse;
    //MeetInfo meetInfo;
    //RelayPositions relayPositions;
}
