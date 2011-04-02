package ru.swimmasters.domain;


import org.hibernate.annotations.Type;
import org.joda.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
@Entity
public class SwimMastersEntry {
    // LEN (9 fields)
    /**
     * The entry time in the swim time format.
     */
    @Type(type="org.joda.time.contrib.hibernate.PersistentDuration")
    Duration entryTime;

    /**
     * Reference to the EVENT element using the id attribute.
     * Required field.
     * XML: eventId
     */
    @ManyToOne(optional = false)
    SwimMastersEvent event;

    //Integer heatId;
    //Integer lane;

    /**
     * This attribute is used for the entry status information.
     */
    EntryStatus status = EntryStatus.REGULAR;

    //Integer ageGroupId;
    //Integer entryCourse;
    //MeetInfo meetInfo;

    // TODO: relay positions
    //RelayPositions relayPositions;

    // SwimMasters
    @Id
    Integer id;

    @ManyToOne(optional = false)
    SwimMastersAthlete athlete;

    //LocalTimeStamp mandateTimestamp; ??
}
