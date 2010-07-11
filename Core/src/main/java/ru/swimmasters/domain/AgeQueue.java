package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Ordered queue of applications of the same age group.  First applications in
 * the head of the queue are leads.  Applications are retrieved from the queue
 * inside monolithic bricks. Leads are always in the first brick, all the next
 * bricks are single-sized.
 *
 * @see Application#compareTo(Application) for ordering rules.
 *
 * @author dedmajor
 * @since 13.06.2010
 */
public class AgeQueue {
    private final AgeGroup ageGroup;
    private final int leadsInAgeGroup;
    private final Queue<Application> applications;

    private boolean leadsRemoved;

    public AgeQueue(AgeGroup ageGroup, int leadsInAgeGroup) {
        this.ageGroup = ageGroup;
        this.leadsInAgeGroup = leadsInAgeGroup;
        leadsRemoved = false;
        applications = new PriorityQueue<Application>();
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void putApplication(Application application) {
        if (application.getAgeGroup() != ageGroup) {
            throw new IllegalArgumentException("application " + application
                    + " does not fit into the age group " + ageGroup);
        }
        applications.add(application);
    }

    public boolean hasMoreApplications() {
        return !applications.isEmpty();
    }

    public List<Application> nextBrick() {
        int size = nextBrickSize();

        if (size > applications.size()) {
            throw new IllegalArgumentException(
                    "queue has only " + applications.size() + " applications, but requested: " + size);
        }

        List<Application> result = new ArrayList<Application>(size);

        for (int i = 0; i < size; i++) {
            result.add(applications.remove());
        }

        leadsRemoved = true;

        return result;
    }

    public int nextBrickSize() {
        return leadsRemoved
                ? 1
                : Math.min(applications.size(), leadsInAgeGroup);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("ageGroup", ageGroup).
                append("applications.size", applications.size()).
                toString();
    }
}
