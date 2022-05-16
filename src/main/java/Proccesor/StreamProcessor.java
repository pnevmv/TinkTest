package Proccesor;

import Connection.CandleStream;
import Connection.TradeStream;
import Data.CompaniesForTrading;
import Data.Company;
import Data.CompanyNotFoundException;
import Data.IndexType;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;

public class StreamProcessor {
    CompaniesForTrading companies;
    CandleStream candleStream;
    TradeStream tradeStream;

    Candle curCandle;
    Company curCandleCompany;

    public StreamProcessor(CompaniesForTrading companies, CandleStream candleStream, TradeStream tradeStream){
        this.companies = companies;
        this.tradeStream = tradeStream;
        this.candleStream = candleStream;
    }


    public void process(MarketDataResponse marketDataResponse) {
        if(marketDataResponse.hasCandle()){
            try {
                curCandle = marketDataResponse.getCandle();
                curCandleCompany = companies.getByFigi(curCandle.getFigi());

                for(IndexType index : IndexType.values()){
                    curCandleCompany.setIndexValue(index
                            , IndexCalculators.getCalcByIndex(index).calculateIndex(curCandleCompany, curCandle));
                }



            } catch (CompanyNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
