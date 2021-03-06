package ru.swimmasters.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Comparator;

/**
 * TODO: unique event, athlete
 * 
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

    @NotNull
    @ManyToOne(targetEntity = Athlete.class)
    private Athlete participant; // TODO: FINA names it competitor

    @NotNull
    @ManyToOne(targetEntity = Event.class)
    private Event event;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private Float declaredTime; // TODO: rename to seed time? Not always available?


    public Application() {
        // hibernate and JAXB should pass
    }

    public Application(Event event, Athlete participant) {
        if (event.getDiscipline().getGender() != participant.getGender()) {
            throw new IllegalArgumentException("athlete " + participant + " cannot participate in "
                    + event + " because of the gender");
        }
        this.event = event;
        this.participant = participant;
    }

    public Integer getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    @XmlIDREF
    public Athlete getParticipant() {
        return participant;
    }

    public void setParticipant(Athlete participant) {
        this.participant = participant;
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

    public boolean isComplete() {
        return declaredTime != null;
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        // TODO: strong validation
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


    public static class DeclaredTimeComparator implements Comparator<Application>, Serializable {
        private static final long serialVersionUID = -2634540637576682685L;

        @Override
        public int compare(Application first, Application second) {
            return new CompareToBuilder()
                    .append(first.getDeclaredTime(), second.getDeclaredTime())
                    .toComparison();
        }
    }
}
