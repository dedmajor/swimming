package ru.swimmasters.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * After registration is closed, Meet Register goes as a source
 * for a credentials committee.
 *
 * @author dedmajor
 * @since 27.07.2010
 */
@XmlRootElement
@XmlType
public class MeetRegister {
    private Meet meet;

    private Set<Discipline> disciplines = new HashSet<Discipline>();
    private Set<Athlete> athletes = new HashSet<Athlete>();

    // TODO: FIXME: find better place?
    @XmlTransient
    private final List<StartList> startLists = new ArrayList<StartList>();
    @XmlTransient
    private final List<TotalRanking> totalRankings = new ArrayList<TotalRanking>();

    public MeetRegister(Meet meet) {
        this.meet = meet;
        for (Event event : meet.getEvents()) {
            disciplines.add(event.getDiscipline());
            for (Application application : event.getApplications()) {
                athletes.add(application.getParticipant());
            }
        }
    }

    public MeetRegister() {
        // JAXB should pass
    }

    @XmlElement(required = true)
    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    @XmlElement(required = true)
    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @XmlElement(required = true)
    public Set<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(Set<Athlete> athletes) {
        this.athletes = athletes;
    }

    public List<StartList> getStartLists() {
        return startLists;
    }

    public void addStartList(StartList startList) {
        startLists.add(startList);
    }

    public List<TotalRanking> getTotalRankings() {
        return totalRankings;
    }

    public void addTotalRanking(TotalRanking ranking) {
        totalRankings.add(ranking);
    }
}
