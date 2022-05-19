package Proccesor.Calculators;

import Data.Company;
import Data.IndexType;

import Proccesor.MoneyQuotationProcessor;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for calculation RSI
 * RSI = 100 - 100 / (1 + RS)
 * RS = AverageOf(P)/ AverageOf(N)
 * P - sum of differences between close of neighbour candles, when close(i) > close(i-1)
 * N - sum of differences between close of neighbour candles, when close(i - 1) > close(i)
 */
public class RSICalculator implements IndexCalculator {

    @Override
    public double calculateIndex(Company company, Candle candle) {
        BigDecimal positive = BigDecimal.ZERO;
        BigDecimal negative = BigDecimal.ZERO;
        MoneyQuotationProcessor moneyProc = new MoneyQuotationProcessor();
        BigDecimal curCandleCLose = moneyProc.convertFromQuation(candle.getClose());
        List<BigDecimal> historyInNum = new ArrayList<>();

        for(Candle c : company.getIndexByType(IndexType.RSI).getHistoryAsList()){
            historyInNum.add(moneyProc.convertFromQuation(c.getClose()));
        }

        for(int i = 1; i < historyInNum.size(); i++) {
            if (historyInNum.get(i).compareTo(historyInNum.get(i - 1)) > 0) // pos += close(i) - close(i - 1)
                positive.add(
                        historyInNum.get(i)
                            .subtract(historyInNum.get(i - 1))
                );
            if (historyInNum.get(i).compareTo(historyInNum.get(i - 1)) < 0) // neg += close(i - 1) - close(i)
                negative.add(
                        historyInNum.get(i - 1)
                            .subtract(historyInNum.get(i))
                );
        }

        if(curCandleCLose.compareTo(historyInNum.get(historyInNum.size() - 1)) > 0) //pos += candle.close - close(last)
            positive.add(
                    curCandleCLose
                            .subtract(historyInNum.get(historyInNum.size() - 1)));

        if(curCandleCLose.compareTo(historyInNum.get(historyInNum.size() - 1)) < 0) //neg += close(last) - candle.close
            negative.add(
                    historyInNum.get(historyInNum.size() - 1)
                            .subtract(curCandleCLose)
            );

        company.getIndexByType(IndexType.RSI).updateHistory(candle); //update history of candles

        return  RSICalcFromNegAndPos(positive, negative).doubleValue();
    }


    private BigDecimal RSICalcFromNegAndPos(BigDecimal pos, BigDecimal neg){
        return BigDecimal.valueOf(100).subtract(
                BigDecimal.valueOf(100).divide(
                        BigDecimal.ONE.add(
                                pos.divide(neg)  // (pos/size) / (neg/size) = pos/neg
                        )
                )
        );
    }


}
