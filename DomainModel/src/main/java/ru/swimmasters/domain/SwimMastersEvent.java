package ru.swimmasters.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: dedmajor
 * Date: 3/13/11
 */
@Entity
public class SwimMastersEvent implements Event {
    // LEN (15 fields)
    // AGEGROUPS
    // daytime
    // eventid = id
    // FEE

    @Column(nullable = false)
    EventGender eventGender = EventGender.ALL;

    // TODO: Len: HEATS
    @OneToMany(mappedBy = "event")
    List<SwimMastersEntry> entries;

    // maxentries

    /**
     *  The number of the event. The event numbers should be unique
     *  over all events of a meet. The EVENT objects of the different rounds
     *  for the same event may have the same event number.
     *  Required field.
     */
    @Column(nullable = false)
    Integer number;
    // order
    // preveventid
    // round
    // run

    @ManyToOne
    SwimMastersSwimStyle swimStyle;
    // TIMESTANDARDREFS
    // timing


    // SwimMasters

    @Id
    Integer id;
    // meet_id - not used yet
    // discipline_id = swimstyle
    // date - moved to session
    // number = number


    @Override
    public EventGender getEventGender() {
        return eventGender;
    }

    @Override
    public SwimStyle getSwimStyle() {
        return swimStyle;
    }

    @Override
    public EventEntries getEntries() {
        return new CheckedEventEntries() {
            @NotNull
            @Override
            public List<Entry> getAll() {
                return new ArrayList<Entry>(entries);
            }
        };
    }
}
