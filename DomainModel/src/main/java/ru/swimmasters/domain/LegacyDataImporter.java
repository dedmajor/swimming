package ru.swimmasters.domain;

import org.joda.time.LocalDate;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        System.out.println("Converting clubs");
        convertClub();
        System.out.println("Converting athletes");
        convertAthlete();
        // TODO: FIXME: hardcoded meet_id: 114
        transaction.commit();
    }

    private void convertClub() {
        new LegacyQueryTemplate("select \"id\", \"name\", \"city_id\", \"akvsp_code\", \"en_name\" from club" ){
            @Override
            protected void handleResultSet(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    SwimmastersClub club = new SwimmastersClub();
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
                    SwimmastersAthlete athlete = new SwimmastersAthlete();
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
                    athlete.club = entityManager.find(SwimmastersClub.class, resultSet.getInt("club_id"));
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
