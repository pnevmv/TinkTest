package Connection;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;

import java.util.Queue;

public interface CandleSource {
    Queue<HistoricCandle> uploadCandles(String figi, CandleInterval candleInterval, int candleStepsBack);

}
