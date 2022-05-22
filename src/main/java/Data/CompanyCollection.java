package Data;

import Connection.CandleSource;
import Connection.CandleStream;
import Exceptions.CompanyNotFoundException;
import com.sun.tools.javac.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    public void stopTradingByFigi(String figi, CandleStream candleStream) {
        this.companies.get(figi).tradeOff(candleStream);
    }

    public void stopTradingForAll(CandleStream candleStream) {
        for (Company company: companies.values()) {
            company.tradeOff(candleStream);
        }
    }

    public void startTradingForAll(CandleSource candleSource) {
        for (Company company: this.companies.values()) {
            company.startTrade(candleSource);
        }
    }

    public Company getByFigi(String figi) throws CompanyNotFoundException {
        if (!companies.containsKey(figi)) throw new CompanyNotFoundException("There's no company with figi: " + figi);
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
        List<String> figis = new ArrayList<>();
        for (Company company : companies.values()) {
            if (company.getIsTrading()) figis.add(company.getFigi());
        }
        try {
            if (figis.isEmpty()) throw new CompanyNotFoundException("There's no companies");
        } catch (CompanyNotFoundException exception) {
        }
        return figis;
    }

    public Company getCompanyByFigi(String figi) {
        return this.companies.get(figi);
    }

    public void printCompanies() {
        for (Company company: companies.values()) {
            System.out.println(company.toString());
        }
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
