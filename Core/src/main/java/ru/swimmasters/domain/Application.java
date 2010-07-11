package ru.swimmasters.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * User: dedmajor
 * Date: Jun 2, 2010
 */
@XmlType
@Entity
@Table(name="APPLICATION", uniqueConstraints = @UniqueConstraint(columnNames = {"event", "participant"}))
public class Application implements Comparable<Application> {
    @XmlAttribute(required = true)
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @XmlElement(required = true)
    @NotNull
    @ManyToOne(targetEntity = Athlete.class)
    private Athlete participant;

    @NotNull
    @ManyToOne(targetEntity = Event.class)
    private Event event;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Float declaredTime;
    

    public Application() {
        // hibernate and JAXB should pass
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

    @XmlElement(required = true)    
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

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.event = (Event) parent;
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

    /**
     * First goes the fastest application. For applications of the same time,
     * first goes the youngest athlete's application (for athletes of the same
     * age first goes the athlete with name starting with Z, athlete with name
     * starting with A goes the last).
     */
    @Override
    public int compareTo(Application application) {
        return new CompareToBuilder()
                .append(declaredTime, application.getDeclaredTime())
                // TODO: move comparison to athlete:
                .append(application.getParticipant().getBirthYear(), participant.getBirthYear())
                .append(application.getParticipant().getName(), participant.getName())
                .toComparison();
    }
}
