package ru.swimmasters.domain;

import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Reads data from a previous version of the database and persists into the current one.
 *
 * Current one MUST be empty, therefore one need to drop and create the database again.
 *
 * User: dedmajor
 * Date: 3/7/11
 */
public class LegacyDataImporter {
    private final EntityManager entityManager;
    private final Connection legacyConnection;
    private final String meetId;

    private SwimMastersMeet meet;
    private List<SwimMastersAgeGroup> groups;

    public LegacyDataImporter(EntityManager entityManager, Connection legacyConnection, String meetId) {
        this.entityManager = entityManager;
        this.legacyConnection = legacyConnection;
        this.meetId = meetId;
    }

    public static void main(String[] args) throws SQLException, IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("first argument must be meet id (e. g. bsvc-dubna-2011)");
        }
        Properties properties = new Properties();
        properties.load(LegacyDataImporter.class.getResourceAsStream("/META-INF/database.properties"));

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        Properties legacyProperties = new Properties();
        legacyProperties.load(LegacyDataImporter.class.getResourceAsStream("/META-INF/legacy_database.properties"));
        dataSource.setDatabaseName(legacyProperties.getProperty("pg.databaseName"));
        dataSource.setUser(legacyProperties.getProperty("pg.user"));
        dataSource.setPassword(legacyProperties.getProperty("pg.password"));
        dataSource.setServerName(legacyProperties.getProperty("pg.serverName"));

        Connection legacyConnection = dataSource.getConnection();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit", properties);
        EntityManager persistenceUnit = factory.createEntityManager();
        try {
            new LegacyDataImporter(persistenceUnit, legacyConnection, args[0]).runImport();
        } finally {
            legacyConnection.close();
            persistenceUnit.close();
            factory.close();
        }
    }

    public void runImport() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        System.out.println("Converting venues");
        convertVenues();
        System.out.println("Converting clubs");
        convertClub();
        System.out.println("Converting athletes");
        convertAthlete();
        System.out.println("Converting swim styles (disciplines)");
        convertSwimStyles();


        System.out.println("Converting meet");
        convertMeet();
        System.out.println("Creating age groups");
        createAgeGroups();
        System.out.println("Converting events");
        convertEvents();
        System.out.println("Converting entries (heats)");
        convertEntries();
        transaction.commit();
    }

    private void createAgeGroups() {
        groups = SwimMastersAgeGroups.createDefaultGroups();
        for (SwimMastersAgeGroup group : groups) {
            entityManager.persist(group);
        }
    }

    private void convertVenues() {
        new LegacyQueryTemplate("select *, city.name as city_name from venue left join city on city.id=city_id") {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimMastersPool pool = new SwimMastersPool(resultSet.getInt("lanes"));
                    pool.setId(resultSet.getInt("id"));
                    pool.setName(resultSet.getString("name"));
                    pool.setAddress(resultSet.getString("address"));
                    pool.setCourse(resultSet.getInt("course"));
                    pool.setCity(resultSet.getString("city_name"));
                    entityManager.persist(pool);
                }
            }
        }.run();
    }

    private void convertMeet() {
        meet = entityManager.find(SwimMastersMeet.class, meetId);
        if (meet == null) {
            createMeet();
        }
    }

    private void createMeet() {
        // TODO: FIXME: sql injections
        new LegacyQueryTemplate("select * from meet where short_name='" + meetId + "'") {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    meet = new SwimMastersMeet(entityManager.find(SwimMastersPool.class, resultSet.getInt("venue_id")));
                    meet.setId(meetId);
                    meet.setSmId(resultSet.getInt("id"));
                    meet.setName(resultSet.getString("name"));
                    entityManager.persist(meet);
                }
            }
        }.run();
    }

    private void convertEntries() {
        new LegacyQueryTemplate("select heat.id, event_id, " +
                "extract(epoch from entry_time) * 1000 as entry_time, athlete_id, relay_team_id " +
                "from heat " +
                "left join event on event_id = event.id where meet_id='" + meet.getSMId() + "';") {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimMastersEntry entry = new SwimMastersEntry();
                    entry.id = resultSet.getLong("id");
                    entry.event = entityManager.find(SwimMastersEvent.class, resultSet.getLong("event_id"));

                    Long entryTimeMs = resultSet.getLong("entry_time");
                    if (entryTimeMs != 0L) {
                        entry.entryTime = new Duration(entryTimeMs);
                    }

                    long athleteId = resultSet.getLong("athlete_id");
                    if (athleteId != 0) {
                        appendAthlete(entry, athleteId);
                    }
                    long relayId = resultSet.getLong("relay_team_id");
                    if (relayId != 0) {
                        appendRelay(entry, relayId);
                    }


                    entityManager.persist(entry);
                }
            }


        }.run();
    }

    private void appendRelay(SwimMastersEntry entry, long relayId) {
        SwimMastersRelayTeam team = entityManager.find(SwimMastersRelayTeam.class, relayId);
        if (team == null) {
            team = convertRelayTeam(relayId);
            entityManager.persist(team);
            for (RelayPosition position : team.getRelayPositions().getAll()) {
                entityManager.persist(position);
            }

        }
        entry.relayTeam = team;
    }

    private void appendAthlete(SwimMastersEntry entry, long athleteId) {
        SwimMastersAthlete athlete = entityManager.find(SwimMastersAthlete.class, athleteId);
        if (athlete == null) {
            throw new IllegalStateException("athlete cannot be null for entry " + entry.id);
        }
        SwimMastersMeetAthlete meetAthlete = (SwimMastersMeetAthlete) meet.getMeetAthletes().getByAthlete(athlete);
        if (meetAthlete == null) {
            meetAthlete = meet.addAthlete(athlete);
            entityManager.persist(meetAthlete);
        }

        entry.athlete = meetAthlete;
    }

    private void convertEvents() {
        // TODO: FIXME: order?2
        new LegacyQueryTemplate("select event.id, discipline_id, discipline.name, sex_id, date, number from event " +
                "left join discipline on discipline_id = discipline.id " +
                "where meet_id = " + meet.getSMId()) {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                int number = 1;
                while (resultSet.next()) {
                    String dateString = resultSet.getString("date");
                    SwimMastersSession session = null;
                    if (dateString != null) {
                        LocalDate date = new LocalDate(dateString);
                        session = (SwimMastersSession) meet.getSessions().findByDate(date);
                        if (session == null) {
                            session = new SwimMastersSession(meet, date);
                            meet.addSession(session);
                            entityManager.persist(session);
                        }
                    }
                    SwimMastersSwimStyle style = new SwimMastersSwimStyle();
                    style.name = resultSet.getString("name");
                    normalizeStyleName(style);
                    SwimMastersSwimStyle styleByNormalizedName = findStyleByNormalizedName(style);
                    if (styleByNormalizedName == null) {
                        throw new IllegalStateException("cannot find discipline, style " + style);
                    }
                    SwimMastersEvent event = new SwimMastersEvent(session, styleByNormalizedName);
                    event.id = resultSet.getLong("id");
                    event.eventGender = EventGender.ALL; // TODO: FIXME
                    event.number = resultSet.getInt("number") > 0 ? resultSet.getInt("number") : number;
                    number++;
                    event.setAgeGroups(groups);


                    int sexId = resultSet.getInt("sex_id");
                    switch (sexId) {
                        case 1:
                            event.eventGender = EventGender.MALE;
                            break;
                        case 2:
                            event.eventGender = EventGender.FEMALE;
                            break;
                        case 3:
                            event.eventGender = EventGender.MIXED;
                            break;
                        default:
                            throw new IllegalStateException("unknown gender: " + sexId);
                    }

                    System.out.println(event);

                    entityManager.persist(event);
                }
            }
        }.run();
    }

    private void convertClub() {
        new LegacyQueryTemplate("select id, name, city_id, akvsp_code, en_name from club" ){
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimMastersClub club = new SwimMastersClub();
                    club.id = resultSet.getInt("id");
                    club.name = resultSet.getString("name");
                    // TODO: FIXME:
                    //club.city_id = resultSet.getInt("city_id");
                    //club.akvsp_code = resultSet.getInt("akvsp_code");
                    club.englishName = resultSet.getString("en_name");

                    System.out.println(club);

                    entityManager.persist(club);
                }
            }
        }.run();
    }

    private void convertAthlete() {
        new LegacyQueryTemplate("select id, name_first, name_middle, name_last, " +
                "dob, club_id, sex_id, en_name_first, en_name_middle, en_name_last, " +
                "passport_series, passport_number, passport_issued_by, passport_issued_on, " +
                "phone, email " +
                "from athlete") {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimMastersAthlete athlete = new SwimMastersAthlete();
                    athlete.id = resultSet.getLong("id");
                    if (athlete.id == 0L) {
                        continue;
                    }
                    athlete.firstName = resultSet.getString("name_first");
                    athlete.middleName = resultSet.getString("name_middle");
                    athlete.lastName = resultSet.getString("name_last");
                    String birthDate = resultSet.getString("dob");
                    if (birthDate == null) {
                        // TODO: FIXME: it cannot be null
                        //throw new IllegalStateException("birth date cannot be null, athlete " + athlete.id);
                        System.err.println("birth date cannot be null, athlete " + athlete.id);
                        continue;
                    }
                    athlete.birthDate = new LocalDate(birthDate);
                    switch (resultSet.getInt("sex_id")) {
                        case 1:
                            athlete.gender = Gender.MALE;
                            break;
                        case 2:
                            athlete.gender = Gender.FEMALE;
                            break;
                        default:
                            throw new IllegalStateException("gender is invalid, athlete " + athlete.id);
                    }
                    athlete.club = entityManager.find(SwimMastersClub.class, resultSet.getInt("club_id"));
                    athlete.englishFirstName = resultSet.getString("en_name_first");
                    athlete.englishMiddleName = resultSet.getString("en_name_middle");
                    athlete.englishLastName = resultSet.getString("en_name_last");
                    athlete.passport = resultSet.getString("passport_number") != null
                            ? (resultSet.getString("passport_series") + " " + resultSet.getString("passport_number")).trim()
                            : null;
                    athlete.passportIssuedBy = resultSet.getString("passport_issued_by");
                    String passportIssuedOn = resultSet.getString("passport_issued_on");
                    if (passportIssuedOn != null) {
                        athlete.passportIssuedOn = new LocalDate(passportIssuedOn);
                    }
                    athlete.phone = resultSet.getString("phone");
                    athlete.email = resultSet.getString("email");

                    System.out.println(athlete);
                    entityManager.persist(athlete);
                }
            }
        }.run();
    }

    private SwimMastersRelayTeam convertRelayTeam(final long id) {
        final SwimMastersRelayTeam result = new SwimMastersRelayTeam();
        new LegacyQueryTemplate("select * from relay_team where id=" + id) {
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    result.meet = meet;
                    result.id = id;
                    result.newRelayPosition(getRelayAthlete(resultSet.getLong("stage1")), 1);
                    result.newRelayPosition(getRelayAthlete(resultSet.getLong("stage2")), 2);
                    result.newRelayPosition(getRelayAthlete(resultSet.getLong("stage3")), 3);
                    result.newRelayPosition(getRelayAthlete(resultSet.getLong("stage4")), 42);
                    result.name = resultSet.getString("team_name");
                }

            }
        }.run();
        if (result.id > 0) {
            return result;
        }
        throw new IllegalArgumentException("cannot find relay relayTeam with id = " + id);
    }

    private SwimMastersAthlete getRelayAthlete(long stageAthleteId) {
        SwimMastersAthlete result = entityManager.find(SwimMastersAthlete.class, stageAthleteId);
        if (result == null) {
            throw new IllegalStateException("athlete " + stageAthleteId + " not found");
        }
        return result;
    }

    private void convertSwimStyles() {
        new LegacyQueryTemplate("select id, stroke_id, sex_id, length, name, name_abbr, " +
                "name_plain_abbr from discipline" ){
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimMastersSwimStyle style = new SwimMastersSwimStyle();
                    style.id = resultSet.getInt("id");
                    style.distance = resultSet.getInt("length");
                    // sex_id is ignored and set in event
                    style.name = resultSet.getString("name");
                    normalizeStyleName(style);
                    SwimMastersSwimStyle foundStyle = findStyleByNormalizedName(style);
                    if (foundStyle != null) {
                        // will find later
                        continue;
                    }
                    switch (resultSet.getInt("stroke_id")) {
                        case 0: // | все стили            | все
                            style.stroke = Stroke.UNKNOWN;
                            break;
                        case 1: // | вольный стиль        | в/с
                            style.stroke = Stroke.FREE;
                            break;
                        case 2: // | на спине             | н/с
                            style.stroke = Stroke.BACK;
                            break;
                        case 3: // | брасс                | бр
                            style.stroke = Stroke.BREAST;
                            break;
                        case 4: // | баттерфляй           | батт
                            style.stroke = Stroke.FLY;
                            break;
                        case 5: // | комплексное плавание | к/п
                            style.stroke = Stroke.MEDLEY;
                            break;
                        case 6: // | вольный стиль        | эст. в/с
                            style.stroke = Stroke.FREE;
                            turnToRelay(style);
                            break;
                        case 7: // | комбинированная      | эст. к/п
                            style.stroke = Stroke.MEDLEY;
                            turnToRelay(style);
                            break;
                        case 8: // | эстафета             | эст
                        default:
                            throw new IllegalStateException("don't know how to handle stroke");
                    }
                    style.nameAbbr = resultSet.getString("name_plain_abbr");

                    System.out.println(style);

                    entityManager.persist(style);
                }
            }
        }.run();
    }

    @Nullable
    private SwimMastersSwimStyle findStyleByNormalizedName(SwimMastersSwimStyle style) {
        try {
            return (SwimMastersSwimStyle) entityManager.createQuery("from SwimMastersSwimStyle where name=:name")
                    .setParameter("name", style.name).getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }

    private static void normalizeStyleName(SwimMastersSwimStyle style) {
        style.name = style.name.replace(" женщины", "");
        style.name = style.name.replace(" мужчины", "");
        style.name = style.name.replace(" смешанная", "");
    }

    private static void turnToRelay(SwimMastersSwimStyle style) {
        if (!style.name.startsWith("Эстафета 4×")) {
            throw new IllegalStateException("unknown relay");
        }
        style.relayCount = 4;
    }


    private abstract class LegacyQueryTemplate implements Runnable {
        private final String sqlQuery;

        LegacyQueryTemplate(String sqlQuery) {
            this.sqlQuery = sqlQuery;
        }

        @Override
        public void run() {
            try {
                executeStatement();
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

        private void executeStatement() throws SQLException {
            Statement statement = legacyConnection.createStatement();
            try {
                statement.execute(sqlQuery);
                ResultSet resultSet = statement.getResultSet();
                try {
                    handleResultSet(resultSet);
                } finally {
                    resultSet.close();
                }
            } finally {
                statement.close();
            }
        }

        protected abstract void handleResultSet(ResultSet resultSet) throws SQLException;
    }

}
