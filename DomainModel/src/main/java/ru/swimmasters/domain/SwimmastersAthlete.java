package ru.swimmasters.domain;

import org.joda.time.LocalDate;

/**
 * User: dedmajor
 * Date: 3/7/11
 */
public class SwimmastersAthlete {
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
     */
    //SwimmastersClub club;
    // Entries entries;
    // Handicap handicap;

    /**
     * The date of birth for the athlete. If only the year of birth is known,
     * the date should be set to January 1st of that year.
     * Required field.
     */
    LocalDate birthDate;

    /**
     * The first name of the athlete.
     * Required field.
     */
    String firstName;

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
     * SwimMasters: passportSeries + passportNumber.
     */
    String passport;

    //String nation; // enum
    //Results results;
    //Integer swrid;

    // SwimMasters (21 fields):

    Long id;

    String email;
    String phone;

    //Integer clubId;
    //Integer cityId;
    //Integer titleId; // sport master, candidate, etc
    //String englishMiddleName;
    //SmallInteger statusId; // pending, active, dead
    //String passportIssuedBy;
    //LocalDate passportIssuedOn;
    //Integer countryId;
    //LocalDate listingDate;
}
