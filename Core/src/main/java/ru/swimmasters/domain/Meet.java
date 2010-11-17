package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: in LENEX events organized in Sessions groupped by date.
 * TODO: rename pool to venue
 * 
 * @author dedmajor
 * @since 20.06.2010
 */
@XmlType
@Entity
@Table(name = "MEET")
public class Meet {
    @XmlAttribute(required = true)
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne(targetEntity = Pool.class)
    private Pool pool;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate startDate;

    @OneToMany(mappedBy = "meet")
    private List<Event> events = new ArrayList<Event>();

    // TODO: LENEX: reverse association with Club / Athlete / Results
    // we: applications or application results


    public Meet() {
    }

    public Meet(String name) {
        this.name = name;
    }

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public Meet setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement(required = true)
    public Pool getPool() {
        return pool;
    }

    public Meet setPool(Pool pool) {
        this.pool = pool;
        return this;
    }

    @XmlElement(required = true)    
    public LocalDate getStartDate() {
        return startDate;
    }

    public Meet setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    @XmlElement(required = true)
    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setMeet(this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("pool", pool).
                append("startDate", startDate).
                toString();
    }
}
