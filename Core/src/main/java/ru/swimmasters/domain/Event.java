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
    private String name;

    @NotNull
    @ManyToOne(targetEntity = Pool.class)
    private Pool pool;

    @NotNull
    @Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
    private LocalDate holdingDate;

    @OneToMany(mappedBy = "event")
    private List<Application> applications = new ArrayList<Application>();


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public Pool getPool() {
        return pool;
    }

    public Event setPool(Pool pool) {
        this.pool = pool;
        return this;
    }

    public LocalDate getHoldingDate() {
        return holdingDate;
    }

    public Event setHoldingDate(LocalDate holdingDate) {
        this.holdingDate = holdingDate;
        return this;
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
        return result;
    }

    /**
     * @return age queues ordered by their age group, from oldest to youngest
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

        List<AgeQueue> sortedResult = new ArrayList<AgeQueue>();
        sortedResult.addAll(result.values());

        Collections.reverse(sortedResult);

        return sortedResult;
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
