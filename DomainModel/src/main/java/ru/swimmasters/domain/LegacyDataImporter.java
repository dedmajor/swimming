package ru.swimmasters.domain;

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

    public void runImport() throws SQLException {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        convertClub();
        transaction.commit();
    }

    private void convertClub() throws SQLException {
        Statement statement = legacyConnection.createStatement();
        try {
            convertClubStatement(statement);
        } finally {
            statement.close();
        }
    }

    private void convertClubStatement(Statement statement) throws SQLException {
        statement.execute("select \"id\", \"name\", \"city_id\", \"akvsp_code\", \"en_name\" from club");
        ResultSet resultSet = statement.getResultSet();
        try {
            convertClubResult(resultSet);
        } finally {
            resultSet.close();
        }
    }

    private void convertClubResult(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            SwimmastersClub club = new SwimmastersClub();
            club.id = resultSet.getInt(1);
            club.name = resultSet.getString(2);
            // TODO: FIXME:
            //club.city_id = resultSet.getInt(3);
            //club.akvsp_code = resultSet.getInt(4);
            club.englishName = resultSet.getString(5);

            System.out.println(club);

            if (club.id != 0) {
                entityManager.persist(club);
            }
        }
    }

}
