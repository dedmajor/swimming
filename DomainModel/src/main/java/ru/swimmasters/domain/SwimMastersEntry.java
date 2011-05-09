package ru.swimmasters.domain;


import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.Duration;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;

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

    @ManyToOne
    SwimMastersHeat heat;

    Integer lane;

    @Id
    Long id;

    @ManyToOne(optional = false)
    SwimMastersAthlete athlete;

    //LocalTimeStamp mandateTimestamp; ??

    private static final Comparator<Entry> HEATS_ENTRY_COMPARATOR = new HeatEntryComparator();


    SwimMastersEntry() {
    }

    public SwimMastersEntry(SwimMastersEvent event, SwimMastersAthlete athlete, Duration entryTime) {
        this.entryTime = entryTime;
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
    public EntryStatus getStatus() {
        if (athlete.getApprovalStatus() == ApprovalStatus.APPROVED) {
            return EntryStatus.REGULAR;
        }
        return EntryStatus.REJECTED;
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
        return heat != null && lane != null && heat.getNumber() != 0;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("entryTime", entryTime).
                append("athlete", athlete).
                append("ageGroup", getAgeGroup()).
                toString();
    }

    public static Comparator<Entry> heatEntryComparator() {
        return HEATS_ENTRY_COMPARATOR;
    }

    /**
     * First goes the fastest entry. For entries of the same time,
     * first goes the youngest athlete's entry (for athletes of the same
     * age first goes the athlete with name starting with Z, athlete with name
     * starting with A goes the last).
     * Entries without the time goes last.
     */
    private static class HeatEntryComparator implements Comparator<Entry>, Serializable {
        private static final long serialVersionUID = 467231419266351742L;

        @Override
        public int compare(Entry o1, Entry o2) {
            return new CompareToBuilder()
                    .append(o1.getEntryTime(), o2.getEntryTime())
                    .append(o1.getAthlete().getBirthYear(), o2.getAthlete().getBirthYear())
                    .append(o1.getAthlete().getFullName(), o2.getAthlete().getFullName())
                    .toComparison();
        }
    }
}
