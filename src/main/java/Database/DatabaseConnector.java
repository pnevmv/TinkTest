package Database;

import UI.Console.Console;

import java.sql.*;

public class DatabaseConnector {
    private Connection connection;
    private final String JDBC_DRIVER = "org.sqlite.JDBC";

    public void connect() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/companies.db");
            Console.println("Successful database connection");
        } catch (ClassNotFoundException exception) {
            Console.printError("Driver cannot find!");
        } catch (SQLException exception) {
            Console.printError("Database connection error");
        }
    }

    /**
     * @param sqlStatement SQL statement to be prepared.
     * @param generateKeys Is keys needed to be generated.
     * @return Prepared statement.
     * @throws SQLException When there's exception inside.
     */
    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoKey = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoKey);
            return preparedStatement;
        } catch (SQLException exception) {
            throw new SQLException(exception);
        }
    }

    /**
     * Close prepared statement.
     * @param sqlStatement SQL statement to be closed.
     */
    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;

        try {
            sqlStatement.close();
        } catch (SQLException exception) {
            Console.printError("It's not possible to close prepared statement");
        }
    }

    /**
     * Close connection to database.
     */
    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            Console.println("Database connection finished.");
        } catch (SQLException exception) {
            Console.printError("Database connection error!");
        }
    }

    /**
     * Set commit mode of database.
     */
    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            Console.printError("An error occurred while setting the database transaction mode!");
        }
    }

    /**
     * Set normal mode of database.
     */
    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            Console.printError("An error occurred while establishing normal database mode!");
        }
    }

    /**
     * Commit database status.
     */
    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            Console.printError("An error occurred while validating the new state of the database!");
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            Console.printError("An error occurred while reverting the original state of the database!");
        }
    }

    /**
     * Set save point of database.
     */
    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            Console.printError("Database saving error!");
        }
    }
}
