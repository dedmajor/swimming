package ru.swimmasters.domain;

/**
 * User: dedmajor
 * Date: 3/7/11
 */
public class SwimmastersClub {
    // LEN (14 fields):

    //String type; // club, national team, regional team, unattached

    /**
     *  The official club code given by the national federation.
     *  Only official club codes should be used here!
     */
    //String code;

    /**
     *  The full name of the club or the team.
     *  Required field.
     */
    String name;

    /**
     * The club name in english.
     * XML: name.en
     */
    String englishName;

    /**
     * This value can be used to distinguish different teams of the same club
     * in a meet entries or results file.
     * (This object not used in CLUB objects, which appear in the record list sub tree.)
     */
    //Integer number;

    //String shortName;
    //String englishShortName;

    //String nation; // enum

    /**
     * The code of the regional or local swimming committee.
     * Only official codes should be used here!
     */
    //String region;

    //Athletes athletes;
    //Contact contact;
    //Officials officials;
    //Relays relays;
    //Long swrid;

    // SwimMasters (5 fields):

    Integer id;
    // Integer cityId;
    // Integer akvspCode; // TODO: what is this?
}