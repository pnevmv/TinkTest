package Data;

import java.util.HashMap;
import java.util.List;

public class CompaniesForTrading {
    HashMap<String, Company> companies;

    public Company getByFigi(String figi) throws CompanyNotFoundException {
        if (!companies.containsKey(figi)) throw new CompanyNotFoundException();
        return companies.get(figi);
    }

    public HashMap<String, Company> getCompanies() {
        return this.companies;
    }

    public List<String> getFigis() {
        List<String> figis = List.of();
        for (Company company :companies.values()) {
            figis.add(company.figi);
        }
        return figis;
    }

    public List<String> getFigisOfTradingCompanies() {
        List<String> figis = List.of();
        for (Company company : companies.values()) {
            if (company.isTrading) figis.add(company.figi);
        }
        try {
            if (figis.isEmpty()) throw new CompanyNotFoundException();
        } catch (CompanyNotFoundException exception) {
            System.out.println("Нет трэйдящих компаний"); //TODO: logging
        }
        return figis;
    }

    public Company getCompanyByFigi(String figi) {
        return this.companies.get(figi);
    }
}
