package ru.swimmasters.domain;

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
@Table(name = "EVENT")
public class Event {
    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotNull
    @ManyToOne(targetEntity = Meet.class)
    private Meet meet;

    @NotNull
    @ManyToOne(targetEntity = Discipline.class)
    private Discipline discipline;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate holdingDate;

    @OneToMany(mappedBy = "event")
    private List<Application> applications = new ArrayList<Application>();


    public Event(Meet meet, Discipline discipline) {
        this.meet = meet;
        this.discipline = discipline;
    }

    private Event() {
        // hibernate should pass
    }

    public Integer getId() {
        return id;
    }

    public Meet getMeet() {
        return meet;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

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

    public List<Application> getApplications() {
        return Collections.unmodifiableList(applications);
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
        
        for (AgeQueue queue : buildAgeQueues()) {
            boolean leadsAreOut = false;

            while (queue.hasMoreApplications()) {
                int requiredSpace = leadsAreOut
                        ? 1
                        : Math.min(queue.getRemainingApplicationsCount(), leadsInAgeGroup);

                if (currentHeat == null || !currentHeat.hasMoreSpace(requiredSpace)) {
                    currentHeat = new Heat(this);
                    result.add(currentHeat);
                }

                for (int i = 0; i < requiredSpace; i++) {
                    currentHeat.addApplication(queue.nextApplication());
                }

                leadsAreOut = true;
            }
        }

        Collections.reverse(result);

        return result;
    }

    /**
     * @return age queues ordered by their age group, from youngest to oldest
     */
    private Collection<AgeQueue> buildAgeQueues() {
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
                append("meet", meet).
                append("discipline", discipline).
                append("holdingDate", holdingDate).
                toString();
    }
}
