import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.util.List;

public class Index {
    String name;
    double value;
    CandleInterval candleInterval;
    Timestamp fromDate;
    List<Candle> candleHistory;


    public Index(String name, double value, CandleInterval candleInterval, Timestamp fromDate) {
        this.name = name;
        this.value = value;
        this.candleInterval = candleInterval;
        this.fromDate = fromDate;
    }

    public void getHistory(String figi, CandleSource candleSource) {
        this.candleHistory = candleSource.uploadCandles(figi, candleInterval, fromDate);
    }
}
