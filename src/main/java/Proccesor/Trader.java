package Proccesor;

import Connection.Connector;
import Connection.TradeStream;
import Data.Company;
import Data.CompanyCollection;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 *
 */

public class Trader {
    private Connector connector;
    private TradeStream tradeStream;
    private CompanyCollection companies;

    public Trader(Connector connector){
        this.tradeStream = connector.getTradeStream();
        this.companies = connector.getCompanies();
    }

    public void trade(Company company, Candle candle, double probability) {}

   public int calculateLotsToTrade(double probability, Candle candle){
        return 0;
   }
}
