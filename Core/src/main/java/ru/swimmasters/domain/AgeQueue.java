package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Ordered queue of applications of the same age group.
 *
 * @see Application#compareTo(Application) for ordering rules.
 *
 * @author dedmajor
 * @since 13.06.2010
 */
public class AgeQueue {
    private AgeGroup ageGroup;
    private Queue<Application> applications;
    private boolean leadsRemoved;

    public AgeQueue(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
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

    public Application nextApplication() {
        Application result = applications.remove();
        leadsRemoved = true;
        return result;
    }

    /**
     * @return true if head of the queue (lead) is already removed
     */
    public boolean isLeadsRemoved() {
        return leadsRemoved;
    }

    public List<Application> nextApplicationsBrick(int size) {
        if (size > applications.size()) {
            throw new IllegalArgumentException(
                    "queue has only " + applications.size() + " applications, but requested: " + size);
        }

        List<Application> result = new ArrayList<Application>(size);

        for (int i = 0; i < size; i++) {
            result.add(nextApplication());
        }

        return result;
    }

    public int getRemainingApplicationsCount() {
        return applications.size();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("ageGroup", ageGroup).
                append("applications.size", applications.size()).
                toString();
    }
}
