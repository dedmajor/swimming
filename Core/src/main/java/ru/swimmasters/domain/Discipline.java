package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author dedmajor
 * @since 20.06.2010
 */
@Entity
@Table(name = "DISCIPLINE")
public class Discipline {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    @Enumerated
    private Gender gender;

    @NotNull
    private String name;

    @NotNull
    private Integer distance;


    public Gender getGender() {
        return gender;
    }

    public Discipline setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public String getName() {
        return name;
    }

    public Discipline setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDistance() {
        return distance;
    }

    public Discipline setDistance(Integer distance) {
        this.distance = distance;
        return this;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("gender", gender).
                append("name", name).
                append("distance", distance).
                toString();
    }
}
