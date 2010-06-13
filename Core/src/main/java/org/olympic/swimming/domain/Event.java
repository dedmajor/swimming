package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * User: dedmajor
 * Date: Jun 1, 2010
 */
@Entity
public class Event {
    @Id
    private Integer id;

    @NotNull
    private String name;

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

    public String getName() {
        return name;
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

    /**
     * TODO: add test for ordering check
     *
     * @return age queues ordered by their age group
     */
    public Collection<AgeQueue> makeAgeQueues() {
        Map<AgeGroup, AgeQueue> result = new EnumMap<AgeGroup, AgeQueue>(AgeGroup.class);

        for (Application application : applications) {
            AgeQueue queue = result.get(application.getAgeGroup());
            if (queue == null) {
                queue = new AgeQueue(application.getAgeGroup());
                result.put(application.getAgeGroup(), queue);
            }

            queue.putApplication(application);
        }

        return result.values();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("name", name).
                append("pool", pool).
                append("holdingDate", holdingDate).
                toString();
    }
}
