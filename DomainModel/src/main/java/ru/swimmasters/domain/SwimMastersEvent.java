package ru.swimmasters.domain;

import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: dedmajor
 * Date: 3/13/11
 */
@Entity
public class SwimMastersEvent implements Event {
    // LEN (15 fields)
    /*
        Element EVENT
        This element contains all information of an event. For events with finals, there has to be an EVENT element
        for each round.

        AGEGROUPS o - The AGEGROUPS collection contains the descriptions for the age
                                            groups in this event. For Open/Senior events, AGEGROUPS is only
                                           needed with one AGEGROUP element as a placeholder for the
                                          RANKINGS element (for places in result lists).
        daytime t - The daytime of the start of the event.
        eventid n r Every event needs to have an id attribute, so that it can be referenced
                   by ENTRY and RESULT objects. The id attribute has to be unique
                  over all EVENT objects of all sessions of a meet.
        FEE o - The entry fee for this event. If there are global fees per athlete, relay
               and/or meet, the FEE elements in the MEET element should be used.
        gender e - The gender of the event. The default value is all (A). Other values
                  allowed are male (M), female (F) and mixed (X). Mixed is only used
                 for relays.
        HEATS o - Collection with all heats in the event.
        maxentries n - The maximum number of entries per club in this event. To limit the
                      number of entries per athlete or relay, use the maxentries attribute in
                     the MEET element.
        number n r The number of the event. The event numbers should be unique over
                  all events of a meet. The EVENT objects of the different rounds for the
                 same event may have the same event number.
        order n - This value can be used to define the order of the events within a
                 session if it is not by the event number and if there are no start times
                for the events.
        preveventid n - This value is a reference to a previous event's id. (e.g. the prelims
                       events for final events). The default value is -1 and means, that there
                      was no previous event.
        round e - The following values are allowed here:
                 * TIM: This is the default value. Used for an event with timed finals.
                * FHT: Fastest heats of an event with timed finals. Events with this
               value for round should always refer to the corresponding timed
              final event, which should be of the same distance, stroke and age
             groups. Events with round set to FHT only make sence for the
            schedule and ENTRY objects, but never to be used for RESULT's.
           * FIN: This is used for finals including A, B, C, ... finals.
            * SEM: for semi finals.
           * QUA: for quarterfinals.
          * PRE: for prelims.
         * SOP: Swim-Off after prelims.
        * SOS: Swim-Off after semi-finals.
        * SOQ: Swim-Off after quarterfinals.
        run n - Used if there is more than one swim-off necessary. Default value is 1.
        SWIMSTYLE o r The SWIMSTYLE element contains information about distance and
                     stroke of the event.
        TIMESTANDARDREFS o - A list of references to TIMESTANDARDREF elements with references
                            to time standard lists to be used for this event.
        timing - This value is optional. If not available, the value from the MEET
                element is used. Acceptable values are:
               * AUTOMATIC: A full automatic timing system was used.
              * MANUAL3: Timing was done with three manual times per lane.
             * MANUAL1: Timing was done with one manual time per lane.
     */

    @ManyToMany
    private final List<SwimMastersAgeGroup> ageGroups = new ArrayList<SwimMastersAgeGroup>();

    @Column(nullable = false)
    EventGender eventGender = EventGender.ALL;

    // TODO: Len: HEATS
    @OneToMany(mappedBy = "event")
    private final List<SwimMastersEntry> entries = new ArrayList<SwimMastersEntry>();

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
    Long id;
    // meet_id - not used yet
    // discipline_id = swimstyle
    // date - moved to session
    // number = number

    @ManyToOne(optional = false)
    SwimMastersSession session;

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime startListTimestamp;
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    private DateTime rankingsTimestamp;

    @OneToMany(mappedBy = "event")
    private final List<SwimMastersAgeRanking> ageRankings = new ArrayList<SwimMastersAgeRanking>();

    SwimMastersEvent() {
        // hibernate shall pass
    }

    public SwimMastersEvent(SwimMastersSession session) {
        this.session = session;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public EventGender getEventGender() {
        return eventGender;
    }

    @Override
    public SwimStyle getSwimStyle() {
        return swimStyle;
    }

    @Override
    public LocalDate getDate() {
        return session.getDate();
    }

    @Override
    public Meet getMeet() {
        return session.getMeet();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @NotNull
    @Override
    public EventEntries getEntries() {
        return new CheckedEventEntries(this, entries);
    }

    @NotNull
    @Override
    public EventEntries getStartListEntries() {
        List<Entry> result = new ArrayList<Entry>();
        for (Entry entry : entries) {
            if (entry.getStatus() == EntryStatus.REGULAR) {
                result.add(entry);
            }
        }
        return new CheckedEventEntries(this, result);
    }

    @NotNull
    @Override
    public StartListHeats getStartListHeats() {
        return new CheckedStartListHeats(this, (CheckedEventEntries) getStartListEntries());
    }

    @Override
    public boolean isStartListPrepared() {
        for (Entry entry : getStartListEntries().getAll()) {
            if (!entry.isHeatPrepared()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public DateTime getStartListTimestamp() {
        return startListTimestamp;
    }

    @Override
    public AgeGroups getAgeGroups() {
        return new SwimMastersAgeGroups(this, ageGroups);
    }

    @Override
    public boolean isAllHeatsFinished() {
        for (Heat heat : getStartListHeats().getHeatsOrderedByNumber()) {
            if (!heat.isRaceFinished()) {
               return false;
            }
        }
        return true;
    }

    @Override
    public DateTime getRankingsTimestamp() {
        return rankingsTimestamp;
    }

    @NotNull
    @Override
    public AgeRankings getAgeRankings() {
        return new AgeRankings() {
            @Override
            public List<AgeRanking> getAll() {
                return Collections.<AgeRanking>unmodifiableList(ageRankings);
            }
        };
    }

    @Override
    public int getAge(Athlete athlete) {
        return athlete.getAge(session.getDate());
    }

    @Override
    public Pool getPool() {
        return session.getMeet().getPool();
    }

    public void setStartListTimestamp(DateTime startListTimestamp) {
        this.startListTimestamp = startListTimestamp;
    }

    public void setEntries(List<SwimMastersEntry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
    }

    public void setRankingsTimestamp(DateTime rankingsTimestamp) {
        this.rankingsTimestamp = rankingsTimestamp;
    }

    public void setAgeRankings(List<SwimMastersAgeRanking> ageRankings) {
        this.ageRankings.clear();
        this.ageRankings.addAll(ageRankings);
    }

    public SwimMastersEvent addEntry(SwimMastersEntry entry) {
        entries.add(entry);
        return this;
    }

    public void setAgeGroups(List<SwimMastersAgeGroup> ageGroups) {
        // TODO: check intersection?
        this.ageGroups.clear();
        this.ageGroups.addAll(ageGroups);
    }
}
