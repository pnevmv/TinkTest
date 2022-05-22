package Proccesor;

import java.lang.Math;
import java.math.BigDecimal;
import Connection.Connector;
import Connection.TradeStream;
import Data.Company;
import Data.CompanyCollection;
import Data.Deal;
import Exceptions.NotEnoughMoneyToTradeException;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

/**
 * Class that calculate from probability, how many stock it can buy/sell and call TradeStream methods for buying/selling
 * if user hasn't got enough money/stocks for trading, trader will give a signal
 *
 */

public class Trader {
    private Connector connector;
    private TradeStream tradeStream;
    private CompanyCollection companies;
    private InvestApi api;

    public Trader(Connector connector, InvestApi api){
        this.tradeStream = connector.getTradeStream();
        this.api = api;
        this.companies = connector.getCompanies();
    }

    public void trade(Company company, Candle candle, double probability) {
        System.out.println("--GOING TO TRADE--");
        System.out.println(probability);


    }

    public void sellIfStopPrice(Company company, Candle candle){
        double close = MoneyQuotationProcessor.convertFromQuation(candle.getClose()).doubleValue();
        for( Deal d  : company.getOpenDeals().getDealsAsList()){
            if(d.getPrice() <= close){
                tradeStream.sellStock((d.getShareNumber() / company.getLot()), candle.getClose(), company.getFigi());
            }
        }
    }


   public int calculateLotsToBuy(double probability, Candle candle, Company company) throws NotEnoughMoneyToTradeException {
        //todo: учесть стоплос
        int lots = 0;

       BigDecimal closePrice = MoneyQuotationProcessor.convertFromQuation(candle.getClose());
       BigDecimal lotPrice = closePrice.multiply(new BigDecimal(company.getLot()));
       BigDecimal freeMoney = new BigDecimal(company.getFreeMoney());
       BigDecimal probab = new BigDecimal(probability);
       lots = freeMoney.divide(lotPrice).multiply(probab).intValue();
       if(lots == 0) throw new NotEnoughMoneyToTradeException();
       return lots;
   }


    public int calculateLotsToSell(double probability, Candle candle, Company company) {
        double close = MoneyQuotationProcessor.convertFromQuation(candle.getClose()).doubleValue();
        double  companyTakeprofit = company.getTakeProfit() / 100;
        int lots = 0;
        //todo: учесть тейкпрофит
        for(Deal d : company.getOpenDeals().getDealsAsList()){
            if( (d.getPrice() * (1 + companyTakeprofit) > close)){
                lots +=  1;

            }
        }

        return lots;
    }

  // private boolean checkIfCanBuy(){return false;}
  //  private boolean checkIfCanSell(){return false;}
}
