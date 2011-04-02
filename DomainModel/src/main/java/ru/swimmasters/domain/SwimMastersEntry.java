package ru.swimmasters.domain;


import org.hibernate.annotations.Type;
import org.joda.time.Duration;

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
public class SwimMastersEntry implements Entry {
    // LEN (9 fields)
    /*
        Element ENTRY
        This element contains the information for a single entry of an athlete or a relay to a specific round of a meet.

        agegroupid n - Reference to an age group (AGEGROUP element in the
                      AGEGROUPS collection of the EVENT element).
        entrycourse e - This attribute indicates a pool length for the entry time. This is
                       necessary when special seeding rules are used. See section 5.4. for
                      acceptable values.
        entrytime st - The entry time in the swim time format.
        eventid n r Reference to the EVENT element using the id attribute.
        heatid n - Reference to a heat (HEAT element in HEATS collection of the
                  EVENT element).
        lane n - The lane number of the entry.
        MEETINFO o - This element contains the information, about a qualification result for
                    the entry time was achieved.
        RELAYPOSITIONS o - Only for relay entries. This element contains references to the relay
                          swimmers.
        status e - This attribute is used for the entry status information. An empty status
                  attribute means a regular entry. The following values are allowed:
                 * EXH: exhibition swim.
                * RJC: rejected entry
               * WDR: athlete/relay was withdrawn.
        The combination of the attributes eventid, heatid and lane should be unique over all ENTRY objects of the
        same meet.
     */

    /**
     * The entry time in the swim time format.
     */
    @Type(type="org.joda.time.contrib.hibernate.PersistentDuration")
    Duration entryTime;

    /**
     * Reference to the EVENT element using the id attribute.
     * Required field.
     */
    @ManyToOne(optional = false)
    SwimMastersEvent event;

    SwimMastersHeat heat;

    Integer lane;

    /**
     * This attribute is used for the entry status information.
     */
    EntryStatus status = EntryStatus.REGULAR;

    @Id
    Integer id;

    @ManyToOne(optional = false)
    SwimMastersAthlete athlete;

    //LocalTimeStamp mandateTimestamp; ??


    SwimMastersEntry() {
    }

    public SwimMastersEntry(SwimMastersEvent event, SwimMastersAthlete athlete) {

        this.event = event;
        this.athlete = athlete;
    }

    @Override
    public MeetAthlete getAthlete() {
        return athlete;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public Duration getEntryTime() {
        return entryTime;
    }

    @Override
    public Heat getHeat() {
        return heat;
    }

    @Override
    public Integer getLane() {
        return lane;
    }

    @Override
    public boolean isHeatPrepared() {
        return heat != null && lane != null;
    }

    @Override
    public AgeGroup getAgeGroup() {
        return event.getAgeGroups().getFor(athlete);
    }

    public void setHeat(Heat heat) {
        this.heat = (SwimMastersHeat) heat;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
}
