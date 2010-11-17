package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

/**
 * @author dedmajor
 * @since 20.06.2010
 */
@XmlType
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
    private String name; // TODO: SwimStyle / Stroke data type

    @NotNull
    private Integer distance;


    public Discipline() {
    }

    public Discipline(Gender gender, Integer distance, String name) {
        this.gender = gender;
        this.distance = distance;
        this.name = name;
    }

    @XmlID
    public String getDisciplineID() {
        // TODO: FIXME: more reliable ids
        return 'D' + String.valueOf(id);
    }

    public void setDisciplineID(String disciplineID) {
        id = Integer.valueOf(disciplineID.substring(1));
    }

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
