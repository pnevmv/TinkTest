package Data;

import Exceptions.CompanyNotFoundException;
import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class CompanyCollection {
    private HashMap<String, Company> companies;
    private Logger log = LoggerFactory.getLogger(Main.class);

    public CompanyCollection() {
        this.companies = new HashMap<>();
    }

    public void putCompanyByFigi(String figi, Company company) {
        this.companies.put(figi, company);
    }

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
            figis.add(company.getFigi());
        }
        return figis;
    }

    public List<String> getFigisOfTradingCompanies() {
        List<String> figis = List.of();
        for (Company company : companies.values()) {
            if (company.getIsTrading()) figis.add(company.getFigi());
        }
        try {
            if (figis.isEmpty()) throw new CompanyNotFoundException();
        } catch (CompanyNotFoundException exception) {
            log.info("Возникла проблема: нет трэйдящих компаний");
        }
        return figis;
    }

    public Company getCompanyByFigi(String figi) {
        return this.companies.get(figi);
    }

    public int getNumberOfCompanies() {
        return this.companies.size();
    }

    public int getNumberOfTradingCompanies() {
        int count = 0;
        for (Company company : companies.values()) {
            if (company.getIsTrading()) count++;
        }
        return count;
    }

    public boolean isContainsFigi(String figi){
        return companies.containsKey(figi);
    }

    @Override
    public String toString() {
        return "Кол-во выбранных компаний: " + getNumberOfCompanies()
                + "Кол-во трейдящих компаний: " + getNumberOfTradingCompanies();
    }

    public void removeByFigi(String figi){
        companies.remove(figi);
    }
}
