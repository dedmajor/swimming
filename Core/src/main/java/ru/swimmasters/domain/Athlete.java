package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@XmlType
@Entity
@Table(name = "ATHLETE")
public class Athlete {
    @XmlAttribute(required = true)
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    @NotNull
    private Integer birthYear;


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


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("birthYear", birthYear).
                toString();
    }
}
