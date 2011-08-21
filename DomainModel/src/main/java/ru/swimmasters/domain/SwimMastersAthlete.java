package ru.swimmasters.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import sun.security.pkcs11.P11TlsKeyMaterialGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * User: dedmajor
 * Date: 3/7/11
 */
@Entity
public class SwimMastersAthlete implements Athlete {
    /**
     * The club or team for the athlete, when he swam the record.
     * (These elements/objects are allowed in a record list sub tree only.)
     * SwimMasters: required field.
     */
    @ManyToOne(optional = false)
    SwimMastersClub club;

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
        return lastName + ", " + firstName;
    }

    /**
     * Returns the age of athlete by Dec, 31 of the given date.
     */
    @Override
    public int getAge(LocalDate date) {
        return date.getYear() - getBirthYear();
    }

    @Override
    public Club getClub() {
        return club;
    }

    @Override
    public int getBirthYear() {
        return birthDate.getYear();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).
                append("firstName", firstName).
                append("lastName", lastName).
                append("birthDate", birthDate).
                toString();
    }

    public static Comparator<Athlete> getNameComparator() {
        return new AthleteNameComparator();
    }

    private static class AthleteNameComparator implements Comparator<Athlete>, Serializable {
        private static final long serialVersionUID = 3261855627341544471L;

        @Override
        public int compare(Athlete o1, Athlete o2) {
            return o1.getFullName().compareTo(o2.getFullName());
        }
    }
}
