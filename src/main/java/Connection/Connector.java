package Connection;

import Data.CompanyCollection;
import Data.CompanyNotFoundException;
import Proccesor.StreamProcessor;
import ru.tinkoff.piapi.core.InvestApi;

/**
 * Class for unary requests (initialisations, verifications etc)
 */
public class Connector {
    private final TradeStream tradeStream;
    private final CandleStream candleStream;

    public Connector(InvestApi api, CompanyCollection companies) {
        this.tradeStream = new TradeStream(api, companies);
        this.candleStream = new CandleStream(api, companies);
    }

    public TradeStream getTradeStream() {
        return this.tradeStream;
    }

    public CandleStream getCandleStream() {
        return this.candleStream;
    }

    public void initializeStreams(StreamProcessor streamProcessor) throws CompanyNotFoundException, OutNumberOfReconnectAttemptsException {
        tradeStream.initialize(streamProcessor);
        candleStream.initialize(streamProcessor);
    }


}

/*
-проверка работоспособности биржи
-верификации компании по фиги
-получение портфолио
-поля: tradeStream, candleStream
-инициализация стримов свеч и торговли
-обработка исключений
*/
