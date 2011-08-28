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

    private SwimMastersMeet meet;
    private List<SwimMastersAgeGroup> groups;

    public LegacyDataImporter(EntityManager entityManager, Connection legacyConnection) {
        this.entityManager = entityManager;
        this.legacyConnection = legacyConnection;
    }

    public static void main(String[] args) throws SQLException, IOException {
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
            new LegacyDataImporter(persistenceUnit, legacyConnection).runImport();
        } finally {
            legacyConnection.close();
            persistenceUnit.close();
            factory.close();
        }
    }

    public void runImport() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        System.out.println("Creating age groups");
        createAgeGroups();
        System.out.println("Converting venues");
        convertVenues();
        System.out.println("Converting meet");
        convertMeet();
        System.out.println("Converting clubs");
        convertClub();
        System.out.println("Converting athletes");
        convertAthlete();
        System.out.println("Converting swim styles (disciplines)");
        convertSwimStyles();


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
        // TODO: FIXME: remove hardcode
        if (entityManager.find(SwimMastersPool.class, 32) == null) {
            // 32 |    1781 | Бассейн ЦСК ВВС | Самара, Волжский проспект, 10 |
            // 50 |     8 | http://maps.yandex.ru/-/CZDtjzS
            SwimMastersPool pool;
            pool = new SwimMastersPool();
            pool.setId(32);
            // todo: Meet.setCity
            // todo: Meet.setAddress
            // todo: Meet.setCourse(50)
            pool.setName("Бассейн ЦСК ВВС");
            pool.setLaneMin(1);
            pool.setLaneMax(8);
            entityManager.persist(pool);
        }
    }

    private void convertMeet() {
        // TODO: FIXME: remove hardcode
        meet = entityManager.find(SwimMastersMeet.class, "bsvc-samara-2011");
        if (meet == null) {
            meet = new SwimMastersMeet(entityManager.find(SwimMastersPool.class, 32));
            meet.setId("bsvc-samara-2011");
            meet.setSmId(137);
            meet.setName("I Открытый лично-командный турнир «Black Sepia Volga Cup» по плаванию в категории «Мастерс»");
            entityManager.persist(meet);
        }
    }

    private void convertEntries() {
        new LegacyQueryTemplate("select heat.id, event_id, " +
                "extract(milliseconds from entry_time) as entry_time, athlete_id " +
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

                    SwimMastersAthlete athlete = entityManager.find(SwimMastersAthlete.class, resultSet.getLong("athlete_id"));
                    if (athlete == null) {
                        throw new IllegalStateException("athlete cannot be null for entry " + entry.id);
                    }
                    SwimMastersMeetAthlete meetAthlete = (SwimMastersMeetAthlete) meet.getMeetAthletes().getByAthlete(athlete);
                    if (meetAthlete == null) {
                        meetAthlete = meet.addAthlete(athlete);
                        entityManager.persist(meetAthlete);
                    }

                    entry.athlete = meetAthlete;

                    entityManager.persist(entry);
                }
            }
        }.run();
    }

    private void convertEvents() {
        new LegacyQueryTemplate("select event.id, discipline_id, discipline.name, sex_id, date, number from event " +
                "left join discipline on discipline_id = discipline.id " +
                "where meet_id = " + meet.getSMId()){
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
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
                    SwimMastersEvent event = new SwimMastersEvent(session);
                    event.id = resultSet.getLong("id");
                    event.eventGender = EventGender.ALL; // TODO: FIXME
                    event.number = resultSet.getInt("number");
                    event.setAgeGroups(groups);

                    SwimMastersSwimStyle style = new SwimMastersSwimStyle();
                    style.name = resultSet.getString("name");
                    normalizeStyleName(style);
                    event.swimStyle = findStyleByNormalizedName(style);
                    if (event.swimStyle == null) {
                        throw new IllegalStateException("cannot find discipline, event " + event.id);
                    }

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
