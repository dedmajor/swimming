package ru.swimmasters.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * User: dedmajor
 * Date: 3/13/11
 */
@Entity
public class SwimMastersSwimStyle {
    // LEN (6 fields)

    /**
     * The full descriptive name of the swim style if the stroke is unknown
     * (e.g. "5 x 75m Breast with one Arm only").
     */
    String name;

    /**
     *  The distance for the event. For relay events it is the distance
     *  for one single athlete.
     *  Required field.
     */
    @Column(nullable = false)
    Integer distance;

    /**
     * The number of swimmers per entry / result. Value 1 means, that it is an individual event.
     * All other values mean, that it is a relay event.
     * Required field.
     */
    @Column(nullable = false)
    Integer relayCount = 1;

    /**
     * Required field.
     */
    @Column(nullable = false)
    Stroke stroke;

    // swimstyleid = id
    // technique

    // SwimMasters (Discipline, 7 fields)

    /**
     * The id attribute is important for SWIMSTYLE objects, where the stroke attribute
     * is "UNKNOWN". In this case, the id should be a unique value to help to
     * identify the swim style.
     */
    @Id
    Integer id;
    // name = name
    // length = distance * relayCount
    // stroke_id = stroke
    // sex_id
    Gender gender;
    String nameAbbr;
    String namePlainAbbr;

}
