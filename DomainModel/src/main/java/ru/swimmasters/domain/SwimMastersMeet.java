package ru.swimmasters.domain;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.*;

/**
 * User: dedmajor
 * Date: 5/9/11
 */
@Entity
public class SwimMastersMeet implements Meet {
    /*

        SwimMasters Legacy:

        id             | integer                | not null default nextval('meet_id_seq'::regclass)
        venue_id       | integer                | not null
        timing_id      | smallint               | not null default 1
        name           | character varying(128) |
        start_date     | date                   |
        end_date       | date                   |
        short_name     | character varying(64)  |
        akvsp_approved | smallint               | not null default 0

        Len:

        Element MEET
        This element contains all information of one meet, including events, athletes, relays, entries and results.
        Element/Attribute Type
        AGEDATE o - The date to be used to calculate the age of athletes. The default value
                                          is the date of the first session and type by year of birth only.
        altitude n - Height above sea level of the meet city.
        city s r The name of the city where the meet was run.
        city.en si - Name of meet city in english.
        CLUBS o - Collection of clubs of the meet.
        CONTACT o - The contact address of the meet organizer.
        course e - The size of the pool. See section 5.4. for acceptable values. If the
                  attribute is not available, all SESSION objects need to have a course
                 attribute.
        deadline d - The date for the entry deadline.
        deadlinetime t - The exact time for the entry deadline.
        FEES o - Fees used for this meet. On this level, different global fees for clubs,
                athletes and relays are allowed. If there are fees that have to be paid
               per entry, the FEE element in the EVENT objects should be used.
        hostclub s - The executing federation or club of the meet (e.g. the German
                    Swimming Federation, if the European Champ was held in Berlin).
        hostclub.url s - A website url, that directs to the executing club for the meet
        maxentries n - The maximum number of entries per athlete or relay. To limit the
                      number of entries per event and club, use the maxentries attribute in
                     the EVENT element.
        name s r The name of the meet. Normally the name should not contain a full
                date (maybe the year only) and/or a city or pool name.
        name.en si - Meet name in english.
        nation e r The three letter code of the nation of the meet city.
        number s - The sanction number for the meet by the federation.
        organizer s - The organisation which promotes the meet (e.g. FINA for the World
                     Championships).
        organizer.url s - A website url, that directs to the organizer of the meet.
        POINTTABLE o - Description of the point table used for scoring.
        POOL o - Details about the pool where the meet took place.
        QUALIFY o - Details about how qualification times for entries are defined.
        result.url s - A website url, that directs to the results page. This should be a deep
                      (direct) link to the result lists and not the general website of a meet.
        SESSIONS o r Description of all events grouped by session.
        state s - The code of the state of the meet city.
        swrid uid - The global unique meet id given by swimrankings.net.
        timing e - The type of timing for a meet. Acceptable values are:
                  * AUTOMATIC: A full automatic timing system was used.
                 * MANUAL3: Timing was done with three manual times per lane.
                * MANUAL1: Timing was done with one manual time per lane.
        type e - The meet type. The following values are allowed:
        * The default value is empty. This applies for normal meets that are
        run according to the FINA rules.
        * All other values depend on definitions of national federations.
     */

    @Id
    String id;

    Integer smId;

    @Column(nullable = false)
    String name;

    @ManyToOne(optional = false)
    SwimMastersPool pool;

    @OneToMany(mappedBy = "meet")
    List<SwimMastersSession> sessions = new ArrayList<SwimMastersSession>();

    // TODO: establish consistency between sesssions and athletes?
    @OneToMany(mappedBy = "meet")
    List<SwimMastersMeetAthlete> athletes = new ArrayList<SwimMastersMeetAthlete>();

    @OneToMany(mappedBy = "meet")
    List<SwimMastersRelayTeam> relayTeams = new ArrayList<SwimMastersRelayTeam>();

    SwimMastersMeet() {
    }

    public SwimMastersMeet(SwimMastersPool pool) {
        this.pool = pool;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Pool getPool() {
        return pool;
    }

    @Override
    public String getCity() {
        return pool.getCity();
    }

    @Override
    public int getCourse() {
        return pool.getCourse();
    }

    @Override
    public Sessions getSessions() {
        return new Sessions() {
            @Override
            public List<Session> getAll() {
                return Collections.<Session>unmodifiableList(sessions);
            }

            @Override
            public Session findByDate(LocalDate date) {
                for (Session session : sessions) {
                    if (session.getDate().equals(date)) {
                        return session;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public LocalDate getStartDate() {
        if (sessions.isEmpty()) {
            throw new IllegalStateException();
        }
        return sessions.get(0).getDate();
    }

    @Override
    public LocalDate getEndDate() {
        if (sessions.isEmpty()) {
            throw new IllegalStateException();
        }
        return sessions.get(sessions.size() - 1).getDate();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSMId() {
        return smId;
    }

    public void setSmId(Integer smId) {
        this.smId = smId;
    }

    public void addSession(SwimMastersSession session) {
        sessions.add(session);
    }

    @Override
    public Events getEvents() {
        return new Events() {

            @Override
            public List<Event> getAll() {
                List<Event> result = new ArrayList<Event>();
                for (Session session : sessions) {
                    result.addAll(session.getEvents().getAll());
                }
                return result;
            }
        };
    }

    @Override
    public MeetAthletes getMeetAthletes() {
        return new MeetAthletes() {

            @Override
            public Meet getMeet() {
                return SwimMastersMeet.this;
            }

            @Override
            public Collection<MeetAthlete> getAll() {
                return Collections.<MeetAthlete>unmodifiableCollection(athletes);
            }

            @Override
            public List<MeetAthlete> getAllSortedByAthleteName() {
                List<MeetAthlete> result = new ArrayList<MeetAthlete>();
                result.addAll(athletes);
                Collections.sort(result, new Comparator<MeetAthlete>() {
                    @Override
                    public int compare(MeetAthlete o1, MeetAthlete o2) {
                        return SwimMastersAthlete.getNameComparator().compare(o1.getAthlete(), o2.getAthlete());
                    }
                });
                return result;
            }

            @Override
            public MeetAthlete getByAthlete(Athlete athlete) {
                for (MeetAthlete meetAthlete : athletes) {
                    if (meetAthlete.getAthlete().equals(athlete)) {
                        return meetAthlete;
                    }
                }
                return null;
            }
        };
    }

    @Override
    public RelayTeams getRelayTeams() {
        return new RelayTeams() {
            @Override
            public Collection<RelayTeam> getAll() {
                return Collections.<RelayTeam>unmodifiableCollection(relayTeams);
            }
        };
    }

    public SwimMastersMeetAthlete addAthlete(SwimMastersAthlete swimMastersAthlete) {
        SwimMastersMeetAthlete result = new SwimMastersMeetAthlete(this, swimMastersAthlete);
        this.athletes.add(result);
        return result;
    }
}
