package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@Entity
@Table(name="APPLICATION", uniqueConstraints = @UniqueConstraint(columnNames = {"event", "participant"}))
public class Application implements Comparable<Application> {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne(targetEntity = Athlete.class)
    private Athlete participant;

    @NotNull
    @ManyToOne(targetEntity = Event.class)
    private Event event;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Float declaredTime;
    

    private Application() {
        // hibernate should pass
    }

    public Application(Event event, Athlete participant) {
        this.event = event;
        this.participant = participant;
    }

    public Integer getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public Athlete getParticipant() {
        return participant;
    }

    public Float getDeclaredTime() {
        return declaredTime;
    }

    public Application setDeclaredTime(Float declaredTime) {
        this.declaredTime = declaredTime;
        return this;
    }

    public AgeGroup getAgeGroup() {
        return AgeGroup.forAge(participant.getAge(event.getHoldingDate().getYear()));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("event.id", event.getId()).
                append("participant", participant).
                append("declaredTime", declaredTime).
                append("ageGroup", getAgeGroup()).
                toString();
    }

    public int compareTo(Application application) {
        // TODO: FIXME: if time is equal, then order by age / name?
        return declaredTime.compareTo(application.getDeclaredTime());
    }
}
