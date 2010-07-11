package ru.swimmasters.domain;

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

    public boolean hasMoreSpace(int athletesCount) {
        return applications.size() + athletesCount <= event.getPool().getLanesCount();
    }

    public void addApplication(Application application) {
        if (!application.getEvent().equals(event)) {
            throw new IllegalArgumentException("cannot add application from another event: " + application);
        }
        if (!hasMoreSpace()) {
            throw new IllegalStateException("no more space for athletes");
        }
        applications.add(application);
    }

    /**
     * Calls {@link #addApplication} for each application in brick
     * @param applicationsBrick applications to add
     */
    public void addAllApplications(List<Application> applicationsBrick) {
        for (Application application : applicationsBrick) {
            addApplication(application);
        }
    }

    public List<Application> getApplications() {
        return Collections.unmodifiableList(applications);
    }
        
    public int getApplicationsCount() {
        return applications.size();
    }

    /**
     * @return true if this heat has more than one application (athlete)
     */
    public boolean isCompetitive() {
        return applications.size() > 1;
    }

    public List<Athlete> getAthletes() {
        List<Athlete> result = new ArrayList<Athlete>(applications.size());
        for (Application application : applications) {
            result.add(application.getParticipant());
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

    /**
     * Removes all applications from the heat.
     * @param applicationsBrick applications to be removed.
     */
    public void removeAllApplications(List<Application> applicationsBrick) {
        if (!applications.containsAll(applicationsBrick)) {
            throw new IllegalArgumentException(
                    "heat does not contain all the requested applications: " + applicationsBrick);
        }
        applications.removeAll(applicationsBrick);
    }
}
