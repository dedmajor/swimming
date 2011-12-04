package ru.swimmasters.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * In the meet sub tree, this element contains information about a club,
 * including athletes and relays with their entries and/or results.
 *
 * TODO: In the record list sub tree, the element contains information
 * about the club or nation of record holders.
 *
 * User: dedmajor
 * Date: 3/7/11
 */
@Entity
public class SwimMastersClub implements Club {
    // LEN (14 fields):

    //String type; // club, national relayTeam, regional relayTeam, unattached

    /**
     *  The official club code given by the national federation.
     *  Only official club codes should be used here!
     */
    //String code;

    /**
     *  The full name of the club or the relayTeam.
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
    //RelayTeams relays;
    //Long swrid;

    // SwimMasters (5 fields):

    @Id
    Integer id;
    // Integer cityId;

    /**
     * Required for creating aggregate result tables.
     */
    // Integer akvspCode;

    @Override
    public String getName() {
        return name;
    }
}
