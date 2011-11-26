package ru.swimmasters.domain;

import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This contains all information of a athlete including all entries and results
 * in the context of a meet sub tree.
 *
 * User: dedmajor
 * Date: 8/21/11
 */
@Entity
public class SwimMastersMeetAthlete implements MeetAthlete {
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

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SwimMastersMeet meet;

    @ManyToOne
    private SwimMastersAthlete athlete;

    @OneToMany(mappedBy = "athlete")
    private List<SwimMastersEntry> entries;

    // SwimMasters:

    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime approvalTimestamp;

    SwimMastersMeetAthlete() {
        // hibernate shall pass
    }

    public SwimMastersMeetAthlete(SwimMastersMeet meet, SwimMastersAthlete athlete) {
        this.meet = meet;
        this.athlete = athlete;
    }

    public SwimMastersMeetAthlete(SwimMastersMeet meet, SwimMastersAthlete athlete, ApprovalStatus approvalStatus) {
        this(meet, athlete);
        this.approvalStatus = approvalStatus;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Athlete getAthlete() {
        return athlete;
    }

    @Override
    public Meet getMeet() {
        return meet;
    }

    @Override
    public AthleteEntries getEntries() {
        return new AthleteEntries() {
            @Override
            public MeetAthlete getAthlete() {
                return SwimMastersMeetAthlete.this;
            }

            @Override
            public Meet getMeet() {
                return meet;
            }

            @NotNull
            @Override
            public Iterable<Entry> getAll() {
                return Collections.<Entry>unmodifiableCollection(entries);
            }
        };
    }

    @Override
    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    @Override
    public DateTime getApprovalTimestamp() {
        return approvalTimestamp;
    }

    @Override
    public Results getResults() {
        List<Result> results = new ArrayList<Result>();
        for (Entry entry : entries) {
            results.add(entry.getResult());
        }
        return new CheckedAthleteResults(results);
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


    public void setApprovalTimestamp(DateTime approvalTimestamp) {
        this.approvalTimestamp = approvalTimestamp;
    }
}
