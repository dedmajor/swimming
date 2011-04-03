package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
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
    Long id;

    @Column(nullable = false)
    private Integer number;

    @OneToMany(mappedBy = "heat")
    public List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean hasNumber() {
        return number != null;
    }

    @Override
    public Entries getEntries() {
        return new CheckedEventEntries() {
            @NotNull
            @Override
            public List<Entry> getAll() {
                return new ArrayList<Entry>(entries);
            }
        };
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
}
