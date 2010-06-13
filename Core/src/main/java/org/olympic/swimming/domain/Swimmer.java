package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO: rename to Athlete?
 *
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@Entity
public class Swimmer {
    @Id
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private Integer birthYear;


    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Swimmer setBirthYear(int birthYear) {
        this.birthYear = birthYear;
        return this;
    }

    public int getAge(int year) {
        return year - birthYear;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("birthYear", birthYear).
                toString();
    }
}
