package Connection;

import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.Queue;

public interface CandleSource {
    Queue<HistoricCandle> uploadCandles(String figi, CandleInterval candleInterval, Timestamp fromDate);
}
