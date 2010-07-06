package ru.swimmasters.domain;

import org.apache.commons.lang.builder.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * User: dedmajor
 * Date: May 23, 2010
 */
@XmlType
@Entity
@Table(name = "POOL")
public class Pool {
    @XmlAttribute(required = true)
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    @Min(2L)
    private Integer lanesCount;


    public Integer getId() {
        return id;
    }

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public Pool setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement(required = true)
    public String getLocation() {
        return location;
    }

    public Pool setLocation(String location) {
        this.location = location;
        return this;
    }

    @XmlElement(required = true)    
    public Integer getLanesCount() {
        return lanesCount;
    }

    public Pool setLanesCount(Integer lanesCount) {
        this.lanesCount = lanesCount;
        return this;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("location", location).
                append("lanesCount", lanesCount).
                toString();
    }
}
