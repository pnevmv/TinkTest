package Proccesor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import Connection.Connector;
import Connection.TradeStream;
import Data.Company;
import Data.CompanyCollection;
import Data.Deal;
import Exceptions.CompanyNotFoundException;
import Exceptions.NotEnoughMoneyToTradeException;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.core.InvestApi;

/**
 * Class that calculate from probability, how many stock it can buy/sell and call TradeStream methods for buying/selling
 * if user hasn't got enough money/stocks for trading, trader will give a signal
 *
 *
 */

public class Trader {
    private final TradeStream tradeStream;
    private final CompanyCollection companies;
    private final InvestApi api;

    public Trader(Connector connector, InvestApi api){
        this.tradeStream = connector.getTradeStream();
        this.api = api;
        this.companies = connector.getCompanies();
    }

    //todo: учесть комиссию

    public void trade(Company company, Candle candle, double probability) throws NotEnoughMoneyToTradeException, CompanyNotFoundException {
        System.out.println("--GOING TO TRADE--");
        System.out.println(probability);

        if(probability > 0) buyLots( probability, candle, company);

        if(probability < 0) sellLots((-1) * probability, candle, company);

    }




    public void sellIfStopPrice(Company company, Candle candle) throws CompanyNotFoundException {
        BigDecimal close = MoneyQuotationProcessor.convertFromQuation(candle.getClose());
        for( Deal d  : company.getOpenDeals().getDealsAsList()){
            //если текущая цена ниже стоп-цены, то продаем все лоты
            if(d.getStopPrice().compareTo(close) >= 0){
                tradeStream.sellStock((d.getLotNumber()), candle.getClose(), company.getFigi(), d);
            }
        }
    }


   public void buyLots(double probability, Candle candle, Company company) throws NotEnoughMoneyToTradeException, CompanyNotFoundException {
        long lots;

       BigDecimal closePrice = MoneyQuotationProcessor.convertFromQuation(candle.getClose());
       // цена лота = цена закрытия * лотность инструмента
       BigDecimal lotPrice = closePrice.multiply(BigDecimal.valueOf(company.getLot()));

       BigDecimal freeMoney =  BigDecimal.valueOf(company.getFreeMoney());
       BigDecimal probab = BigDecimal.valueOf(probability);

       //если хватает только на 1 лот и вероятность больше 60, взять 1 лот
       if(freeMoney.divide(lotPrice, 9, RoundingMode.HALF_DOWN).intValue() == 1 && probability > 0.6) lots =  1;

       /*Доступные деньги / цена лота = доступное количество лотов, умножением на вероятность получаем
       количество лотов пропорциональное вероятности.
       Т.е. при вероятности 50% бот купит инструментов на 50% от количества доступных денег
       (с поправкой на лотность, округление идет в сторону меньшего количества лотов
        */

       else lots = freeMoney.divide(lotPrice, 9, RoundingMode.HALF_DOWN).multiply(probab).intValue();
       System.out.println("Количество свободных денег:" + freeMoney.toString());
       System.out.println("Количество лотов, которые можно бы купить:" + lots);

       /*
       если невозможно купить ни одного лота, и до этого не было куплено ни одного лота, то пердупредить пользователя
       о невозмоности торговли
        */

       if(company.getFreeMoney() < (MoneyQuotationProcessor.convertFromQuation(candle.getClose()).doubleValue() * company.getLot()) && company.getOpenDeals().getDealsAsList().isEmpty()) throw new NotEnoughMoneyToTradeException();

       //запрос на покупку
       if(lots > 0) tradeStream.buyStock(
               lots,
               candle.getClose(),
               company.getFigi()
       );


   }


    public void sellLots(double probability, Candle candle, Company company) throws CompanyNotFoundException {
        BigDecimal close = MoneyQuotationProcessor.convertFromQuation(candle.getClose());
        // множитель для пересчета цены тейкпрофита
        BigDecimal  companyTakeprofit = BigDecimal.valueOf(1 + (company.getTakeProfit() / 100));


        for(Deal d : company.getOpenDeals().getDealsAsList()){
            //если инстумент превысил свой тейкпрофит
            if( (d.getPrice().multiply(companyTakeprofit)).compareTo(close) <= 0){
                /*
                 в случае доступности 1 лота продаем целиком, иначе долю высчитываемую через вероятность
                 (берется целая часть, округление в меньшую сторону
                 */
               if(d.getLotNumber() == 1) System.out.println(1); tradeStream.sellStock(1, candle.getClose(), company.getFigi(), d);
                if (d.getLotNumber() > 1){
                    System.out.println("Продадим пожалуй: " +  (long)(d.getLotNumber() * probability) + " лотов у айди" + d.getId());
                    tradeStream.sellStock(
                            (long)(d.getLotNumber() * probability), //todo:нормальное взятие целой части
                            candle.getClose(),
                            company.getFigi(),
                            d
                    );
                }


            }
        }
    }

}
