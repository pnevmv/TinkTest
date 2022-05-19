package Proccesor.Calculators;

import Data.Company;
import Data.IndexType;

import Proccesor.MoneyQuotationProcessor;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RSICalculator implements IndexCalculator {

    @Override
    public double calculateIndex(Company company, Candle candle) {
        List<Candle> historyOfCandles = company.getIndexByType(IndexType.RSI).getHistoryAsList();
        BigDecimal positive = BigDecimal.ZERO;
        BigDecimal negative= BigDecimal.ZERO;

        MoneyQuotationProcessor moneyProc = new MoneyQuotationProcessor();
        List<BigDecimal> historyInNum = new ArrayList<>();
        for(Candle c : historyOfCandles){
            historyInNum.add(moneyProc.convertFromQuation(c.getClose()));
        }


        for(int i = 1; i < historyInNum.size(); i++) {


            if (historyInNum.get(i).compareTo(historyInNum.get(i - 1)) > 0) {
                positive.add(historyInNum.get(i).subtract(historyInNum.get(i - 1)));
            }
            if (historyInNum.get(i).compareTo(historyInNum.get(i - 1)) < 0) {
                negative.add(historyInNum.get(i - 1).subtract(historyInNum.get(i)));
            }
        }

        if(moneyProc.compare(candle.getClose(), historyOfCandles.get(historyOfCandles.size() - 1).getClose()) > 0){
            //positive.add
        }

        return 0;
    }
}
