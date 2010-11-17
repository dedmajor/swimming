package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;

/**
 * @author dedmajor
 * @since 13.06.2010
 */
public class Heat implements Comparable<Heat> {
    private final Event event;
    private final int number;
    private final List<Application> applications;
    private List<Application> lastAddedApplications;

    public Heat(Event event, int number) {
        this.event = event;
        this.number = number;
        applications = new ArrayList<Application>(event.getPool().getLanesCount());
    }

    public int getNumber() {
        return number;
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

    public boolean containsApplication(Application application) {
        return applications.contains(application);
    }

    /**
     * Assigns lanes to applications, each group is side by side, fastest applications
     * in the center of each group, free lanes (if any) balanced at the left and right.
     */
    private Map<Lane, Application> assignLanes() {
        Map<Lane, Application> result = new HashMap<Lane, Application>(applications.size());
        Queue<Lane> poolLanes = new PriorityQueue<Lane>(event.getPool().getLanes());
        assert poolLanes.size() >= applications.size();

        removeFreeLanesFromLeft(poolLanes);

        Map<AgeGroup, List<Application>> groupedApplications = groupApplicationsByAge();
        
        for (Map.Entry<AgeGroup, List<Application>> ageEntry : groupedApplications.entrySet()) {
            List<Application> centeredApplications = centerApplications(ageEntry.getValue());

            for (Application application : centeredApplications) {
                result.put(poolLanes.remove(), application);
            }
        }
        
        return result;
    }

    private void removeFreeLanesFromLeft(Queue<Lane> poolLanes) {
        int freeLanes = poolLanes.size() - applications.size();
        int freeLanesFromLeft = freeLanes / 2;
        for (int i = 0; i < freeLanesFromLeft; i++) {
            poolLanes.remove();
        }
    }

    private Map<AgeGroup, List<Application>> groupApplicationsByAge() {
        // TODO: copypaste logic for AgeQueue?
        Map<AgeGroup, List<Application>> groupedApplications =
                new EnumMap<AgeGroup, List<Application>>(AgeGroup.class);

        for (Application application : applications) {
            List<Application> applicationsOfSameAge = groupedApplications.get(application.getAgeGroup());
            if (applicationsOfSameAge == null) {
                applicationsOfSameAge = new ArrayList<Application>();
                groupedApplications.put(application.getAgeGroup(), applicationsOfSameAge);
            }

          applicationsOfSameAge.add(application);
        }
        return groupedApplications;
    }

    /**
     * Orders applications by declared time and puts them from the left and right by turns.
     *
     * @param sample unordered list
     * @return list with the fastest applications in the center
     */
    private static List<Application> centerApplications(List<Application> sample) {
        Deque<Application> resultQueue = new LinkedList<Application>();

        List<Application> sortedApplications = new ArrayList<Application>(sample);
        Collections.sort(sortedApplications, new Application.DeclaredTimeComparator());

        boolean insertLeft = true;
        for (Application application : sortedApplications) {
            if (insertLeft) {
                resultQueue.addFirst(application);
            } else {
                resultQueue.addLast(application);
            }
            insertLeft = !insertLeft;
        }
        List<Application> result = new ArrayList<Application>(resultQueue);

        return Collections.unmodifiableList(result);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("event.id", event.getId()).
                append("applications.size", applications.size()).
                append("applications", applications).
                toString();
    }

    public Lane getLaneByApplication(Application appliaction) {
        for (Map.Entry<Lane, Application> entry: assignLanes().entrySet()) {
            if (entry.getValue().equals(appliaction)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("heat " + this + " doesn't contain application " + appliaction);
    }

    @Override
    public int compareTo(Heat o) {
        return Integer.valueOf(number).compareTo(o.getNumber());
    }
}
