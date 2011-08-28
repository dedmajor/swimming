package ru.swimmasters.domain;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
@Entity
// TODO: unique:
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"meet", "number"}))
public class SwimMastersSession implements Session {
    /*
        Len:

        Element SESSION
        This element is used to describe one session of a meet.
        Element/Attribute Type
        Remarks
        course e - With indicating a pool length per session, the global value of the meet
                  can be overridden, e.g. if the prelim sessions are short course and the
                 finals are long course. See section 5.4. for acceptable values.
        date d r The date of the session.
        daytime t - The daytime when the session starts.
        EVENTS o r The events of the session.
        JUDGES o - The judges of the session.
        name s number n r The number of the session. Session numbers in a meet have to be
                         unique.
        officialmeeting t - The daytime when the officials meeting starts.
        POOL o - The details about the pool, if they are different per session. Otherwise
                use the element in MEET.
        teamleadermeeting t - The daytime when the team leaders meeting starts.
        warmupfrom t - The daytime when the warmup starts.
        warmupuntil t - The daytime when the warmup ends.
     */
    @Id
    @GeneratedValue
    Integer id;

    @ManyToOne(optional = false)
    SwimMastersMeet meet;

    @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false)
    int number;

    @OneToMany(mappedBy = "session")
    List<SwimMastersEvent> events;

    SwimMastersSession() {
    }

    public SwimMastersSession(SwimMastersMeet meet, LocalDate date) {
        this.meet = meet;
        this.date = date;
    }

    @Override
    public Meet getMeet() {
        return meet;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public Events getEvents() {
        return new Events() {

            @Override
            public List<Event> getAll() {
                return Collections.<Event>unmodifiableList(events);
            }
        };
    }

    @Override
    public int getNumber() {
        return number;
    }
}
