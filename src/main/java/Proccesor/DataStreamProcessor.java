package Proccesor;

import Data.Company;
import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Data.IndexType;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

/**
 * Class used for process responses from exchange. Used in CandleStream.
 * CandleStream call method process on each response async.
 * In case response is candle, and time from last candle for current company more than given time,
 * Method processes calculate all indexes, Solver calculates probability to buy/sell, based on indexes
 * and after all company with updated indexes and calculated probability will go to trader, that will
 * buy/sell some amount of stocks
 * Final pipeline: CandleStream -> IndexCalculators -> Solver -> Trader
 */
public class DataStreamProcessor {
    CompanyCollection companies;
    Trader trader;

    Candle curCandle;
    Company curCandleCompany;
    IndexCalculators i;
    public DataStreamProcessor(CompanyCollection companies, Trader trader){
        this.companies = companies;
        this.trader = trader;
        this.i = new IndexCalculators();
    }

    /**
    * Method processes calculate all indexes, Solver calculates probability to buy/sell, based on indexes
    * and after all company with updated indexes and calculated probability will go to trader, that will
     * buy/sell some amount of stocks
     * Final pipeline: CandleStream -> IndexCalculators -> Solver -> Trader
     */
    public void process(MarketDataResponse marketDataResponse) {
            if(marketDataResponse.hasCandle()) {
                curCandle = marketDataResponse.getCandle();
                try {
                    curCandleCompany = companies.getByFigi(curCandle.getFigi()); //get company of candle

                    if (checkIfNewCandleForIndex(IndexType.RSI, marketDataResponse))//Check if candle is on new timestap for some index. Different indexes has different timeframes
                    {
                        System.out.println(curCandle);
                        curCandleCompany.getIndexByType(IndexType.RSI).printHistory();

                        //Calculate each index if candle is on new timestap for this index
                        //todo: calculate index only if candle is on new timestap for this index
                        for (IndexType index : IndexType.values()) {
                            curCandleCompany.setIndexValue(index
                                    , i.getCalcByIndex(index).calculateIndex(curCandleCompany, curCandle));
                        }
                        trader.trade(curCandleCompany, curCandle,
                                Solver.solution(curCandleCompany)); //solver calculates probability to buy/sell based on indexes

                    }
                    trader.sellIfStopPrice(curCandleCompany, curCandle);
                } catch (CompanyNotFoundException e) {
                    e.printStackTrace();
                }
            }

    }

    /**
     * Check if new candle older that previous on the index timestap
     */
    private boolean checkIfNewCandleForIndex(IndexType type, MarketDataResponse marketDataResponse){
        try {
            return companies.getByFigi(
                    marketDataResponse.getCandle().getFigi()).getIndexByType(IndexType.RSI)
                    .getTimeOfLastEl() != marketDataResponse.getCandle().getTime().getSeconds();
        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}

//todo: обработать ситуацию, когда у человека меньше денег чем на один лот
