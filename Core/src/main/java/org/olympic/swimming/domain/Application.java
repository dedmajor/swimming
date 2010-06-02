package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
// TODO: unique event and contestant
@Entity
public class Application {
    @Id
    private Integer id;

    @NotNull
    @ManyToOne(targetEntity = Event.class)
    //@JoinColumn
    private Event event;

    @NotNull
    @ManyToOne(targetEntity = Swimmer.class)
    //@JoinColumn
    private Swimmer contestant; // TODO: rename, participant?

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Float declaredTime;


    public Integer getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Swimmer getContestant() {
        return contestant;
    }

    public Float getDeclaredTime() {
        return declaredTime;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("event", event).
                append("contestant", contestant).
                append("declaredTime", declaredTime).
                toString();
    }
}
