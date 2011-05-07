package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * User: dedmajor
 * Date: 4/3/11
 */
//@Entity
public class SwimMastersAgeGroup implements AgeGroup {
    // LEN
    /*
        Element AGEGROUP
        This element contains information about an age range. It is used in events and record lists.

        agegroupid n r Only for events, every AGEGROUP element needs an id, because the
                      objects can be referenced from ENTRY objects. The id has to be
                     unique within an AGEGROUPS collection.
        agemax n r The upper bound of the age range. -1 means no upper bound.
        agemin n r The lower bound of the age range. -1 means no lower bound.
        gender e - In mixed events, the gender can be specifiedin the AGEGROUP
                  objects. Values can be male (M), female (F) or mixed (X). Mixed is
                 only used for relays. This can be useful to define events with gender
                set to all (A), but the ranking is separated. This attribute is not allowed
               in the context of a RECORDLIST or TIMESTANDARDLIST element.
        calculate e - Information for relay events about how the age is calculated:
                     * SINGLE: This is the default value. The age of each relay swimmer
                    has to be in the given range.
                   * TOTAL: The total age of all swimmers has to be in the given range.
        handicap e - The handicap class for the agegroup. This is used to group results by
                    disability categories. Allowed values are:
                   * 1 - 15, 20, 34, 49 standard handicap classes.
        levelmax s - The maximum level (A-Z) of the agegroup. If the value is missing, this
                    means no upper bound.
        levelmin s - The minimum level (A-Z) of the agegroup. If the value is missing, this
                    means no lower bound.
        name s - The name of the age group (e.g. "Juniors").
        RANKINGS o - A collection with references to results ranked in this agegroup.
        type e - The following values are allowed:
                * The default value is empty. This applies for normal meets that are
               run according to the FINA rules.
              * MASTERS: Master results to be used for master rankings/records.
     */

    @Column(nullable = false)
    private final Integer min;
    @Column(nullable = false)
    private final Integer max;

    SwimMastersAgeGroup() {
        min = 0;
        max = 0;
    }

    public SwimMastersAgeGroup(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public boolean containsAge(int age) {
        if (min == AgeGroup.NO_LOWER_BOUND) {
            return age <= max;
        }
        if (max == AgeGroup.NO_UPPER_BOUND) {
            return age >= min;
        }
        return age >= min && age <= max;
    }

    /**
     * Compares groups by min age in increasing order.
     */
    @Override
    public int compareTo(AgeGroup o) {
        return min.compareTo(o.getMin());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SwimMastersAgeGroup that = (SwimMastersAgeGroup) o;

        if (max != null ? !max.equals(that.max) : that.max != null) return false;
        if (min != null ? !min.equals(that.min) : that.min != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).
                append("min", min).
                append("max", max).
                toString();
    }
}
