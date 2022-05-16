package Data;

import Connection.*;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.util.List;

public class Index {
    IndexType indexType;
    double value;
    CandleInterval candleInterval;
    Timestamp fromDate;
    List<Candle> candleHistory;

    public Index(IndexType indexType, double value, CandleInterval candleInterval, Timestamp fromDate) {
        this.indexType = indexType;
        this.value = value;
        this.candleInterval = candleInterval;
        this.fromDate = fromDate;
    }

    public void getHistory(String figi, CandleSource candleSource) {
        this.candleHistory = candleSource.uploadCandles(figi, candleInterval, fromDate);
    }

    public void setValue(double value) {
        this.value = value;
    }
}
