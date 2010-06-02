package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: dedmajor
 * Date: Jun 1, 2010
 */
@Entity
public class Event {
    @Id
    private Integer id;

    @NotNull
    @ManyToOne(targetEntity = Pool.class)
    //@JoinColumn
    private Pool pool;

    @NotNull
    //@Temporal(TemporalType.DATE)
    //@DateTimeFormat(style = "S-")
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate holdingDate;

    @OneToMany(mappedBy = "event")
    private List<Application> applications;


    public Integer getId() {
        return id;
    }

    public Pool getPool() {
        return pool;
    }

    public LocalDate getHoldingDate() {
        return holdingDate;
    }

    public List<Application> getApplications() {
        return applications;
    }
    

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("pool", pool).
                append("holdingDate", holdingDate).
                toString();
    }
}
