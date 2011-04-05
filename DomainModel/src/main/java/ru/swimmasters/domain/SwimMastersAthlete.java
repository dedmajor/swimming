package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This contains all information of a athlete including all entries and results
 * in the context of a meet sub tree.
 *
 * User: dedmajor
 * Date: 3/7/11
 */
@Entity
public class SwimMastersAthlete implements MeetAthlete {
    // LEN (17 fields)
    /*
        Element ATHLETE
        This contains all information of a athlete including all entries and results
        in the context of a meet sub tree.

        athleteid n r The id attribute should be unique over all athletes of a meet. It is
                                            required for ATHLETE objects in a meet sub tree.
        birthdate d r The date of birth for the athlete. If only the year of birth is known, the
                     date should be set to January 1st of that year.
        CLUB (1) o - The club or team for the athlete, when he swam the record.
        ENTRIES (2) o - All entries of the athlete.
        firstname s r The first name of the athlete.
        firstname.en si - The first name in english.
        gender e r Gender of the athlete. Values can be male (M) and female (F).
        HANDICAP o - Information about the handicap classes of a swimmer.
        lastname s r The last name of the athlete.
        lastname.en si - The last name in english.
        license s - The registration number given by the national federation. This number
           should be looked at together with the nation of the club the athlete is
          listed in the Lenex file.
        middlename s - The middle name of the athlete.
        nameprefix s - An optional name prefix. For example for Peter van den Hoogenband,
                      this could be "van den".
        nation e - See table "Nation Codes" for acceptable values.
        passport s - The passport number of the athlete.
        RESULTS (2) o - All results of the athlete.
        swrid uid - The global unique athlete id given by swimrankings.net.
        (1)
        These elements/objects are allowed in a record list sub tree only.
        (2)
        These elements/objects are allowed in a meet sub tree only.
     */

    /**
     * The club or team for the athlete, when he swam the record.
     * (These elements/objects are allowed in a record list sub tree only.)
     * SwimMasters: required field.
     */
    @ManyToOne(optional = false)
    SwimMastersClub club;

    @OneToMany(mappedBy = "athlete")
    List<SwimMastersEntry> entries;

    /**
     * The date of birth for the athlete. If only the year of birth is known,
     * the date should be set to January 1st of that year.
     * Required field.
     */
    @Type(type="org.joda.time.contrib.hibernate.PersistentLocalDate")
    @Column(nullable = false)
    LocalDate birthDate;

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
     * The passport number of the athlete.
     * SwimMasters: to be validated on a mandate committee.
     * SwimMasters: passportSeries + passportNumber.
     */
    String passport;

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

    ApprovalStatus approvalStatus;


    SwimMastersAthlete() {
    }

    public SwimMastersAthlete(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public SwimMastersAthlete(LocalDate birthDate, String firstName, String lastName) {
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        // TODO: presentation stuff?
        return firstName + ", " + lastName;
    }

    /**
     * Returns the age of athlete by Dec, 31 of the given date.
     */
    @Override
    public int getAge(LocalDate date) {
        return date.getYear() - getBirthYear();
    }

    @Override
    public int getBirthYear() {
        return birthDate.getYear();
    }

    @Override
    public Entries getEntries() {
        return new Entries() {
            @NotNull
            @Override
            public List<Entry> getAll() {
                return new ArrayList<Entry>(entries);
            }
        };
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).
                append("firstName", firstName).
                append("lastName", lastName).
                append("birthDate", birthDate).
                toString();
    }
}
