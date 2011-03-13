package ru.swimmasters.domain;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

/**
 * This contains all information of a athlete including all entries and results
 * in the context of a meet sub tree.
 *
 * User: dedmajor
 * Date: 3/7/11
 */
@Entity
public class SwimMastersAthlete implements Athlete {
    // LEN (17 fields):

    /**
     *  The id attribute should be unique over all athletes of a meet.
     *  It is required for ATHLETE objects in a meet sub tree.
     *  Required field.
     */
    //Long athleteId;

    /**
     * The club or team for the athlete, when he swam the record.
     * (These elements/objects are allowed in a record list sub tree only.)
     * SwimMasters: required field.
     */
    @ManyToOne(optional = false)
    SwimMastersClub club;

    // Entries entries;
    // Handicap handicap;

    /**
     * The date of birth for the athlete. If only the year of birth is known,
     * the date should be set to January 1st of that year.
     * Required field.
     */
    @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column(nullable = false)
    public LocalDate birthDate;

    /**
     * The first name of the athlete.
     * Required field.
     */
    public String firstName;

    /**
     *  The first name in english.
     */
    String englishFirstName;

    /**
     * The middle name of the athlete.
     */
    String middleName;

    /**
     * The last name of the athlete.
     * Required field.
     */
    String lastName;

    /**
     * The last name in english.
     */
    String englishLastName;

    /**
     * Gender of the athlete.
     * Required field.
     */
    @Column(nullable = false)
    Gender gender;

    /**
     * The registration number given by the national federation. This number should be looked
     * at together with the nation of the club the athlete is listed in the Lenex file.
     */
    //String licence;

    /**
     * An optional name prefix. For example for Peter van den Hoogenband, this could be "van den".
     */
    //String namePrefix;

    /**
     * The passport number of the athlete.
     * SwimMasters: to be validated on a mandate committee.
     * SwimMasters: passportSeries + passportNumber.
     */
    String passport;

    //String nation; // enum
    //Results results;
    //Integer swrid;

    // SwimMasters (21 fields):

    @Id
    Long id;

    String email;
    String phone;

    //Integer cityId;
    //Integer titleId; // sport master, candidate, etc
    String englishMiddleName;
    //SmallInteger statusId; // pending, active, dead
    String passportIssuedBy;
    @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDate")
    LocalDate passportIssuedOn;
    //Integer countryId;
    //LocalDate listingDate; // TODO: FIXME: why we need this?

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }
}
