package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
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

    public boolean isWorking(InvestApi api) {
        //TradingSchedule = api.getInstrumentsService().
        return true;
    }


}

/*
-проверка работоспособности биржи
-верификации компании по фиги
-получение портфолио
-инициализация стримов свеч и торговли
-обработка исключений
*/
