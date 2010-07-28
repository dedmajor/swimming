package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
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
    private List<Event> events;


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
