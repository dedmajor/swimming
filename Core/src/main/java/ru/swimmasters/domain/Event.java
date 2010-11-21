package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.*;

/**
 * TODO: unique Meet, Discipline
 *
 * TODO: in LENEX, holdingDate is an attribute of the Session, and dayTime for event.
 *
 * User: dedmajor
 * Date: Jun 1, 2010
 */
@XmlType
@Entity
@Table(name = "EVENT")
public class Event {
    @XmlAttribute(required = true)
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne(targetEntity = Meet.class)
    private Meet meet; // TODO: FIXME: is not required?

    @NotNull
    @ManyToOne(targetEntity = Discipline.class)
    private Discipline discipline;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate holdingDate;

    @OneToMany(mappedBy = "event")
    private final List<Application> applications = new ArrayList<Application>();

    // TODO: FIXME: only one constructor?
    public Event(Discipline discipline) {
        this.discipline = discipline;
    }

    // TODO: meet is not required for this class?
    @Deprecated
    public Event(Meet meet, Discipline discipline) {
        this.meet = meet;
        this.discipline = discipline;
    }

    // TODO: FIXME: holding date is a session property, not an event
    @Deprecated
    public Event(Discipline discipline, LocalDate holdingDate) {
        this.discipline = discipline;
        this.holdingDate = holdingDate;
    }

    public Event() {
        // hibernate and JAXB should pass
    }

    public Integer getId() {
        return id;
    }

    @XmlTransient
    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    @XmlIDREF
    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @XmlElement(required = true)
    public LocalDate getHoldingDate() {
        return holdingDate;
    }

    public Event setHoldingDate(LocalDate holdingDate) {
        this.holdingDate = holdingDate;
        return this;
    }

    public Pool getPool() {
        return meet.getPool();
    }

    public Event addApplication(Application application) {
        if (!application.getEvent().equals(this)) {
            throw new IllegalArgumentException("cannot accept application from another event: " + application);
        }
        applications.add(application);
        return this;
    }

    @XmlElement(required = true)
    public List<Application> getApplications() {
        // TODO: not immutable due to jaxb
        //return Collections.unmodifiableList(applications);
        return applications;
    }

    public boolean containsApplication(Application appliaction) {
        return applications.contains(appliaction);
    }
    
    /**
     * Heats are built from the age queue of applications, considering the youngest
     * and fastest athletes first. Then result is reverted to make it be at a natural order.
     *
     * @param leadsInAgeGroup how many leads in each age group must start in one heat
     * @return minimal possible list of heats with respect to the leads rule
     */
    public List<Heat> buildHeats(int leadsInAgeGroup) {
        List<Heat> result = new ArrayList<Heat>();
        Heat currentHeat = null;
        Heat previousBrickHeat = null;
        int number = 0;

        for (AgeQueue queue : buildAgeQueues(leadsInAgeGroup)) {
            while (queue.hasMoreApplications()) {
                previousBrickHeat = currentHeat;

                if (currentHeat == null || !currentHeat.hasMoreSpace(queue.nextBrickSize())) {
                    assert currentHeat == null || currentHeat.isCompetitive();
                    number++;
                    currentHeat = new Heat(this, number);
                    result.add(currentHeat);
                }

                currentHeat.addAllApplications(queue.nextBrick());
            }
        }

        if (currentHeat != null && !currentHeat.isCompetitive()) {
            if (previousBrickHeat == null) {
                throw new IllegalStateException("not enough applications to build competitive heats");
            }
            currentHeat.addAllApplications(previousBrickHeat.removeLastAddedApplications());
        }

        Collections.reverse(result);

        return result;
    }

    /**
     * @return age queues ordered by their age group, from youngest to oldest
     * @param leadsInAgeGroup see {@link #buildHeats}
     */
    private Collection<AgeQueue> buildAgeQueues(int leadsInAgeGroup) {
        Map<AgeGroup, AgeQueue> result = new EnumMap<AgeGroup, AgeQueue>(AgeGroup.class);

        for (Application application : applications) {
            AgeQueue queue = result.get(application.getAgeGroup());
            if (queue == null) {
                queue = new AgeQueue(application.getAgeGroup(), leadsInAgeGroup);
                result.put(application.getAgeGroup(), queue);
            }

            queue.putApplication(application);
        }

        return result.values();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent) {
        this.meet = (Meet) parent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("meet", meet).
                append("discipline", discipline).
                append("holdingDate", holdingDate).
                append("applications.size", applications.size()).
                toString();
    }
}
