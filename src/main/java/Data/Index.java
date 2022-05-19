package Data;

import Connection.*;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.*;

public class Index {
    private final IndexType indexType;
    private double value;
    private final CandleInterval candleInterval;
    private int candleStepsBack;
    private Queue<HistoricCandle> candleHistory;

    public Index(IndexType indexType, double value, CandleInterval candleInterval, int candleStepsBack) {
        this.indexType = indexType;
        this.value = value;
        this.candleInterval = candleInterval;
        this.candleStepsBack = candleStepsBack;
    }

    public double getValue() {
        return value;
    }

    public int getcandleStepsBack() {
        return candleStepsBack;
    }

    public void setCandleHistory(Queue<HistoricCandle> candleHistory) {
        this.candleHistory = candleHistory;
    }

    public void loadHistory(String figi, CandleSource candleSource) {
        this.candleHistory = candleSource.uploadCandles(figi, candleInterval, candleStepsBack);
    }

    public void updateHistory(Candle candle) {

        if(candleHistory.size() > 0) this.candleHistory.remove();
        HistoricCandle c = HistoricCandle.newBuilder() //todo: converting candle to historicalCandle
                .setClose(candle.getClose())
                .setHigh(candle.getHigh())
                .setLow(candle.getLow())
                .setOpen(candle.getOpen())
                .build();

        this.candleHistory.add(c);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public Collection<HistoricCandle> getHistory(){
        return candleHistory;
    }

    public List<HistoricCandle> getHistoryAsList(){
        ArrayList<HistoricCandle> res = new ArrayList<>();
        
        for(HistoricCandle c : candleHistory)   res.add(c);
        return res;
    }
}
