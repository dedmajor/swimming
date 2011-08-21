package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: dedmajor
 * Date: 4/2/11
 */
@Entity
public class SwimMastersHeat implements Heat {
    // LENEX (7 fields)
    /*
        agegroupid n - Reference to an age group (AGEGROUP element in the
                      AGEGROUPS collection of the EVENT element).
        daytime t - The daytime of the start of the event.
        final e - This value is used to identify A, B, ... finals. Allowed are characters A,
                 B, C and D.
        heatid n r The id attribute should be unique over all heats of a meet. It is
                  required when you have ENTRY / RESULT objects that reference to a
                 heat.
        number n r The number of the heat. The heat numbers have to be unique within
                  one event, also in a case when you have A finals in different
                 agegroups.
        order n - This value can be used to define the order of the heats within an event
                 if it is not by the heat number and if there are no start times for the
                heats.
        status e - The status of the heat. Allowed values are:
                  * SEEDED: The heat is seeded
                 * INOFFICIAL: Results are available but not official.
                * OFFICIAL: Results of the heat are official.
     */
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private Integer absoluteNumber;

    @OneToMany(mappedBy = "heat")
    public List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();

    @Transient
    private List<SwimMastersEntry> lastAddedBrick;

    @Column(nullable = false)
    private RaceStatus raceStatus = RaceStatus.NOT_STARTED;

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime startTimestamp;
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime finishTimestamp;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Event getEvent() {
        // TODO: FIXME: store event
        return entries.get(0).getEvent();
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getTotalHeatsInEvent() {
        return getEvent().getStartListHeats().getHeatsOrderedByNumber().size();
    }

    @Override
    public int getAbsoluteNumber() {
        return absoluteNumber;
    }

    public void setAbsoluteNumber(Integer absoluteNumber) {
        this.absoluteNumber = absoluteNumber;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean hasNumber() {
        return number != null;
    }

    @Override
    public Entries getEntries() {
        return new CheckedEventEntries(entries);
    }

    @Override
    public Entry getEntryByLane(int lane) {
        for (Entry entry : entries) {
            Integer entryLane = entry.getLane();
            if (entryLane == null) {
                throw new IllegalStateException();
            }
            if (entryLane == lane) {
                return entry;
            }
        }
        throw new IllegalArgumentException("heat " + id + " has no entry at the lane " + lane);
    }

    @Override
    public boolean isCompetitive() {
        return entries.size() > 1;
    }

    @Override
    public AgeGroup getOldestAgeGroup() {
        AgeGroup result = null;
        for (Entry entry : entries) {
            if (result == null || entry.getAgeGroup().compareTo(result) > 0) {
                result = entry.getAgeGroup();
            }
        }
        if (result == null) {
            throw new IllegalStateException("should be at least one entry");
        }
        return result;
    }

    @Override
    public AgeGroup getYoungestAgeGroup() {
        AgeGroup result = null;
        for (Entry entry : entries) {
            if (result == null || entry.getAgeGroup().compareTo(result) < 0) {
                result = entry.getAgeGroup();
            }
        }
        if (result == null) {
            throw new IllegalStateException("should be at least one entry");
        }
        return result;
    }

    /**
     * If two entries have the same entry time, order is taken from
     * {@link SwimMastersEntry.HeatEntryComparator}.
     */
    @Override
    public Entry getFastestEntry(AgeGroup ageGroup) {
        Entry result = null;
        for (Entry entry : entries) {
            if (entry.getAgeGroup().compareTo(ageGroup) == 0 &&
                    (result == null || SwimMastersEntry.heatEntryComparator().compare(result, entry) > 0)) {
                result = entry;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("we have no heats of group " + ageGroup);
        }
        assert result.getAgeGroup().equals(ageGroup);
        return result;
    }

    @Override
    public RaceStatus getRaceStatus() {
        return raceStatus;
    }

    @Override
    public DateTime getStartTimestamp() {
        return startTimestamp;
    }

    @Override
    public DateTime getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setRaceStatus(RaceStatus raceStatus) {
        this.raceStatus = raceStatus;
    }

    public void setStartTimestamp(DateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setFinishTimestamp(DateTime finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    public void addBrick(List<SwimMastersEntry> brick) {
        entries.addAll(brick);
        lastAddedBrick = brick;
    }

    public List<SwimMastersEntry> removeLastAddedBrick() {
        if (lastAddedBrick == null) {
            throw new IllegalStateException("no applications were added");
        }
        removeAllEntries(lastAddedBrick);
        return lastAddedBrick;
    }

    /**
     * Removes all applications from the heat.
     * @param entriesBrick applications to be removed.
     */
    private void removeAllEntries(Collection<SwimMastersEntry> entriesBrick) {
        if (!entries.containsAll(entriesBrick)) {
            throw new IllegalArgumentException(
                    "heat does not contain all the requested applications: " + entriesBrick);
        }
        entries.removeAll(entriesBrick);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                toString();
    }
}
