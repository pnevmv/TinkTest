package Proccesor;

import Connection.TradeStream;
import Data.Company;
import Data.CompanyCollection;
import ru.tinkoff.piapi.contract.v1.Candle;

/**
 *
 */


public class Trader {
    private TradeStream tradeStream;
    private CompanyCollection companies;

    public Trader(TradeStream tradeStream, CompanyCollection companies){
        this.tradeStream = tradeStream;
        this.companies = companies;
    }

    public void trade(Company company, Candle candle, double propability){}

   public int calculateLotsToTrade(double probability, Candle candle){
        return 0;
   }
}
