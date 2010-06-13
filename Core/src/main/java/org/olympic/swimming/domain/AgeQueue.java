package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class AgeQueue {
    private AgeGroup ageGroup;
    private Queue<Application> applications;

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
        return applications.remove();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("ageGroup", ageGroup).
                append("applications.size", applications.size()).
                toString();
    }
}
