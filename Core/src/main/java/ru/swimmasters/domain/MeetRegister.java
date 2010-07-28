package ru.swimmasters.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dedmajor
 * @since 27.07.2010
 */
@XmlRootElement
@XmlType
public class MeetRegister {
    private Meet meet;

    private Set<Discipline> disciplines = new HashSet<Discipline>();
    private Set<Athlete> athletes = new HashSet<Athlete>();

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
}
