package Proccesor;

import Connection.CandleStream;
import Data.Company;
import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Data.IndexType;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

public class StreamProcessor {
    CompanyCollection companies;
    Trader trader;

    Candle curCandle;
    Company curCandleCompany;

    public StreamProcessor(CompanyCollection companies, Trader trader){
        this.companies = companies;
        this.trader = trader;
    }


    public void process(MarketDataResponse marketDataResponse) {
        if(marketDataResponse.hasCandle()){
            try {
                curCandle = marketDataResponse.getCandle();
                curCandleCompany = companies.getByFigi(curCandle.getFigi());

                for(IndexType index : IndexType.values()){
                    curCandleCompany.setIndexValue(index
                            , IndexCalculators.getCalcByIndex(index).calculateIndex(curCandleCompany, curCandle));
                    curCandleCompany.getIndexByType(index).updateHistory(curCandle);
                }

                trader.trade(curCandleCompany, curCandle, Solver.solution(curCandleCompany));
            } catch (CompanyNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}

//todo: обработать ситуацию, когда у человека меньше денег чем на один лот
