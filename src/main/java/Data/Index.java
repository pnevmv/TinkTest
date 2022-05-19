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
    private Timestamp fromDate;
    private Queue<HistoricCandle> candleHistory;

    public Index(IndexType indexType, double value, CandleInterval candleInterval, Timestamp fromDate) {
        this.indexType = indexType;
        this.value = value;
        this.candleInterval = candleInterval;
        this.fromDate = fromDate;
    }

    public double getValue() {
        return value;
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public void setCandleHistory(Queue<HistoricCandle> candleHistory) {
        this.candleHistory = candleHistory;
    }

    public void loadHistory(String figi, CandleSource candleSource) {
        this.candleHistory = candleSource.uploadCandles(figi, candleInterval, fromDate);
    }

    public void updateHistory(Candle candle) {

        this.candleHistory.remove();
        HistoricCandle c = HistoricCandle.newBuilder()
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
