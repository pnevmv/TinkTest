package Database;

import Data.Company;
import UI.Console.Console;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseManager {
    private final DatabaseConnector databaseConnector;

    private static final String COMPANIES_TABLE = "companies";
    private static final String COMPANIES_TABLE_ID_COLUMN = "id";
    private static final String COMPANIES_TABLE_NAME_COLUMN = "name";

    private final String SELECT_ALL_COMPANIES = "SELECT * FROM " + COMPANIES_TABLE;

    private final String INSERT_COMPANY = "INSERT INTO " +
            COMPANIES_TABLE + " (" +
            COMPANIES_TABLE_ID_COLUMN + ", " +
            COMPANIES_TABLE_NAME_COLUMN + ") VALUES (?, ?)";
    private final String DELETE_COMPANY_BY_ID = "DELETE FROM " + COMPANIES_TABLE +
            " WHERE " + COMPANIES_TABLE_ID_COLUMN + " = ?";

    public DatabaseManager(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Inserts company to table in database
     * @param company to add
     */
    public void insertCompany(Company company) {

        PreparedStatement preparedInsertCompanyStatement = null;

        try {
            databaseConnector.setCommitMode();
            databaseConnector.setSavepoint();

            preparedInsertCompanyStatement = databaseConnector.getPreparedStatement(INSERT_COMPANY, true);
            preparedInsertCompanyStatement.setString(1, company.getFigi());
            preparedInsertCompanyStatement.setString(2, "name" + Math.random());
            if (preparedInsertCompanyStatement.executeUpdate() == 0) throw new SQLException();
            Console.println("Request completed INSERT_COMPANY.");

            databaseConnector.commit();

        } catch (SQLException exception) {
            Console.printError("An error occurred while executing a group of requests to add a new object!\n" + exception.getSQLState() + "\n");
            exception.printStackTrace();
            databaseConnector.rollback();

        } finally {
            databaseConnector.closePreparedStatement(preparedInsertCompanyStatement);
            databaseConnector.setNormalMode();
        }

    }

    /**
     * Create Company.
     * @param resultSet Result set parameters of Band.
     * @return New Company.
     * @throws SQLException When there's exception inside.
     */
    private Company createCompany(ResultSet resultSet) throws SQLException {

        String figi = resultSet.getString(COMPANIES_TABLE_ID_COLUMN);
        String name = resultSet.getString(COMPANIES_TABLE_NAME_COLUMN);

        return new Company(
                figi,
                0,
                0,
                0,
                0
        );

    }

    /**
     * @return List of Companies.
     */
    public HashMap<String, Company> getCollection() {

        HashMap<String, Company> companyCollection = new HashMap<>();
        PreparedStatement preparedSelectAllStatement = null;

        try {

            preparedSelectAllStatement = databaseConnector.getPreparedStatement(SELECT_ALL_COMPANIES, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();

            while (resultSet.next()) {
                Company newCompany = createCompany(resultSet);
                companyCollection.put(newCompany.getFigi(), newCompany);
            }

        } catch (SQLException exception) {
            Console.printError("Request executing error");

        } finally {
            databaseConnector.closePreparedStatement(preparedSelectAllStatement);
        }

        return companyCollection;
    }

    /**
     * Delete Company by figi.
     * @param figi figi of Company.
     */
    public void deleteCompanyByFigi(String figi){

        PreparedStatement preparedDeleteCompanyByFigiStatement = null;

        try {
            preparedDeleteCompanyByFigiStatement = databaseConnector.getPreparedStatement(DELETE_COMPANY_BY_ID, false);
            preparedDeleteCompanyByFigiStatement.setString(1, figi);

            if (preparedDeleteCompanyByFigiStatement.executeUpdate() == 0) Console.println("Updating status 0");

        } catch (SQLException exception) {
            Console.printError("Executing request error: DELETE_BAND_BY_ID!");

        } finally {
            databaseConnector.closePreparedStatement(preparedDeleteCompanyByFigiStatement);
        }
    }

    /**
     * Clear the collection.
     */
    public void clearCollection() {
        Collection<Company> companyCollection = getCollection().values();
        for (Company company : companyCollection) {
            deleteCompanyByFigi(company.getFigi());
        }
    }

}
