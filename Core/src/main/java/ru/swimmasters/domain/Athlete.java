package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@Entity
@Table(name = "ATHLETE")
public class Athlete {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    @NotNull
    private Integer birthYear;


    public String getName() {
        return name;
    }

    public Athlete setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Athlete setBirthYear(int birthYear) {
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
