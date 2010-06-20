package ru.swimmasters.domain;

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
public class Application implements Comparable<Application> {
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
    

    private Application() {
        // hibernate should pass
    }

    public Application(Event event, Swimmer contestant) {
        this.event = event;
        this.contestant = contestant;
    }

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

    public Application setDeclaredTime(Float declaredTime) {
        this.declaredTime = declaredTime;
        return this;
    }

    public AgeGroup getAgeGroup() {
        return AgeGroup.forAge(contestant.getAge(event.getHoldingDate().getYear()));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("event.id", event.getId()).
                append("contestant", contestant).
                append("declaredTime", declaredTime).
                append("ageGroup", getAgeGroup()).
                toString();
    }

    public int compareTo(Application application) {
        // TODO: FIXME: if time is equal, then order by age / name?
        return declaredTime.compareTo(application.getDeclaredTime());
    }
}
