package Proccesor;

import Connection.Connector;
import Connection.TradeStream;
import Data.Company;
import Data.CompanyCollection;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

/**
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
     //   if(probability >= 0.30 && candle.getHigh().getUnits() < company.getMoneyToTrade()){
            var res = api.getOrdersService().postOrderSync(company.getFigi(),
                    1,
                    api.getMarketDataService().getLastPricesSync(List.of(company.getFigi())).get(0).getPrice(),
                    OrderDirection.ORDER_DIRECTION_BUY,
                    connector.findAccount(),
                    OrderType.ORDER_TYPE_MARKET,
                    "knnkfkkkkjkvm"
            );
            System.out.println(res);
      //  }
       // if(probability <= -0.30){
             res = api.getOrdersService().postOrderSync(company.getFigi(),
                    1,
                    api.getMarketDataService().getLastPricesSync(List.of(company.getFigi())).get(0).getPrice(),
                    OrderDirection.ORDER_DIRECTION_SELL,
                    connector.findAccount(),
                    OrderType.ORDER_TYPE_MARKET,
                    "knnkfkkkkjkvm"
            );
            System.out.println(res);
       // }

    }

   public int calculateLotsToTrade(double probability, Candle candle){
        return 0;
   }
}
