package ru.swimmasters.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
    // HEATS
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
    public SwimStyle getSwimStyle() {
        return swimStyle;
    }
}
