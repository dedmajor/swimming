package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class Heat {
    private final Event event;
    private final List<Application> applications;
    private List<Application> lastAddedApplications;

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

    private void addApplication(Application application) {
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
        lastAddedApplications = applicationsBrick;
    }

    public List<Application> getApplications() {
        return Collections.unmodifiableList(applications);
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

    public AgeGroup getYoungestAgeGroup() {
        AgeGroup result = null;
        for (Application application : applications) {
            if (result == null || application.getAgeGroup().compareTo(result) < 0) {
                result = application.getAgeGroup();
            }
        }
        return result;
    }

    /**
     * @return fastest application for specified ageGroup
     * @see Application#compareTo for more accurate ordering definition
     */
    public Application getFastestApplication(AgeGroup ageGroup) {
        Application result = null;
        for (Application application : applications) {
            if (application.getAgeGroup() == ageGroup
                    && (result == null || application.compareTo(result) < 0)) {
                result = application;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("no applications for group " + ageGroup);
        }
        assert result.getAgeGroup() == ageGroup;
        return result;
    }

    public List<Application> removeLastAddedApplications() {
        if (lastAddedApplications == null) {
            throw new IllegalStateException("no applications were added");
        }
        removeAllApplications(lastAddedApplications);
        return lastAddedApplications;
    }

    /**
     * Removes all applications from the heat.
     * @param applicationsBrick applications to be removed.
     */
    private void removeAllApplications(List<Application> applicationsBrick) {
        if (!applications.containsAll(applicationsBrick)) {
            throw new IllegalArgumentException(
                    "heat does not contain all the requested applications: " + applicationsBrick);
        }
        applications.removeAll(applicationsBrick);
    }

    public boolean isLaneOccupied(Lane lane) {
        return assignLanes().containsKey(lane);
    }

    public Application getApplicationByLane(Lane lane) {
        if (!isLaneOccupied(lane)) {
            throw new IllegalArgumentException("lane " + lane + " is not occupied");
        }
        return assignLanes().get(lane);
    }

    private Map<Lane, Application> assignLanes() {
        // TODO: implement me
        Map<Lane, Application> result = new HashMap<Lane, Application>(applications.size());
        List<Lane> poolLanes = event.getPool().getLanes();
        assert poolLanes.size() >= applications.size();

        for (int i = 0; i < applications.size(); i++) {
            result.put(poolLanes.get(i), applications.get(i));
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
