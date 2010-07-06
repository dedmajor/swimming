package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author dedmajor
 * @since 20.06.2010
 */
@XmlType
@Entity
@Table(name = "DISCIPLINE")
public class Discipline {
    @XmlAttribute(required = true)
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

    @XmlElement(required = true)
    public Gender getGender() {
        return gender;
    }

    public Discipline setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public Discipline setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement(required = true)
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
