package org.olympic.swimming.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class Heat {
    private final Event event;
    private List<Application> applications;

    public Heat(Event event) {
        this.event = event;
        applications = new ArrayList<Application>(event.getPool().getLanesCount());
    }

    public boolean hasMoreSpace() {
        return hasMoreSpace(1);
    }

    public boolean hasMoreSpace(int swimmersCount) {
        return applications.size() + swimmersCount <= event.getPool().getLanesCount();
    }

    public void addApplication(Application application) {
        if (!application.getEvent().equals(event)) {
            throw new IllegalArgumentException("cannot add application from another event: " + application);
        }
        if (!hasMoreSpace()) {
            throw new IllegalStateException("no more space for swimmers");
        }
        applications.add(application);
    }

    public List<Application> getApplications() {
        return Collections.unmodifiableList(applications);
    }

    public List<Swimmer> getSwimmers() {
        List<Swimmer> result = new ArrayList<Swimmer>(applications.size());
        for (Application application : applications) {
            result.add(application.getContestant());
        }
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("event.id", event.getId()).
                append("applications.size", applications.size()).
                append("applications", applications).
                toString();
    }
}