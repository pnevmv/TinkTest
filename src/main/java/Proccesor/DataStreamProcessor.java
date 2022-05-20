package Proccesor;

import Data.Company;
import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Data.IndexType;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

public class DataStreamProcessor {
    CompanyCollection companies;
    Trader trader;

    Candle curCandle;
    Company curCandleCompany;

    public DataStreamProcessor(CompanyCollection companies, Trader trader){
        this.companies = companies;
        this.trader = trader;
    }

    public void process(MarketDataResponse marketDataResponse) {
            if(marketDataResponse.hasCandle()
                    && checkIfNewCandleForIndex(IndexType.RSI, marketDataResponse)
            ){
                try {
                    curCandle = marketDataResponse.getCandle();
                    curCandleCompany = companies.getByFigi(curCandle.getFigi());

                    System.out.println(curCandle);
                    curCandleCompany.getIndexByType(IndexType.RSI).printHistory();

                    IndexCalculators i = new IndexCalculators();

                    //trader.trade(curCandleCompany, curCandle, Solver.solution(curCandleCompany));

                    for(IndexType index : IndexType.values()){
                        curCandleCompany.setIndexValue(index
                                , i.getCalcByIndex(index).calculateIndex(curCandleCompany, curCandle));
                        //curCandleCompany.getIndexByType(index).updateHistory(curCandle);
                        //System.out.println(index);
                        //System.out.println(curCandleCompany.getIndexByType(index).getValue());
                    }
                    trader.trade(curCandleCompany, curCandle, Solver.solution(curCandleCompany));
                } catch (CompanyNotFoundException e) {
                    e.printStackTrace();
                }
            }

    }

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
