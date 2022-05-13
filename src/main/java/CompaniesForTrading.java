import java.util.HashMap;

public class CompaniesForTrading {
    HashMap<String, Company> companies;

    public Company getByFigi(String figi) throws CompanyNotFoundException {
        if (!companies.containsKey(figi)) throw new CompanyNotFoundException();
        return companies.get(figi);
    }

    public HashMap<String, Company> getCompanies() {
        return this.companies;
    }
}
