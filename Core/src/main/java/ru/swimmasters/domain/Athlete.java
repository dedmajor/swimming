package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

/**
 * TODO: city / country / club
 * 
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@XmlType
@Entity
@Table(name = "ATHLETE")
public class Athlete {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name; // TODO: firstName, lastName

    @NotNull
    private Integer birthYear;

    @NotNull
    private Gender gender;

    
    public Athlete() {
    }

    public Athlete(String name, Integer birthYear, Gender gender) {
        this.name = name;
        this.birthYear = birthYear;
        this.gender = gender;
    }

    @XmlID
    public String getAthleteID() {
        // TODO: FIXME: more reliable ids
        return 'A' + String.valueOf(id);
    }

    public void setAthleteID(String athleteID) {
        id = Integer.valueOf(athleteID.substring(1));
    }


    @XmlElement(required = true)    
    public String getName() {
        return name;
    }

    public Athlete setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement(required = true)    
    public Integer getBirthYear() {
        return birthYear;
    }

    public Athlete setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
        return this;
    }

    public int getAge(int year) {
        return year - birthYear;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("birthYear", birthYear).
                append("gender", gender).
                toString();
    }
}
