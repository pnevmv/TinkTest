package Connection;
import Data.*;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.StreamProcessor;

import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;


import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;


public class CandleStream implements CandleSource{
    private final CompanyCollection companies;
    private final MarketDataStreamService marketStreamServ;
    private MarketDataSubscriptionService stream;
    private final InvestApi api;


    public CandleStream(InvestApi api, CompanyCollection companies){
        marketStreamServ = api.getMarketDataStreamService();
        this.companies = companies;
        this.api = api;
    }

    public void initialize(StreamProcessor processor) throws CompanyNotFoundException {
        Consumer<Throwable> streamError = e -> {System.out.println(e.toString()); }; //todo: logger, correct reconnection
        stream = marketStreamServ.newStream("Candles", processor::process, streamError);
        stream.subscribeCandles(companies.getFigisOfTradingCompanies(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
    }

    public void reconnect() throws OutNumberOfReconnectAttemptsException {
    }

    public void checkConnection(){

    }
    public void updateSubscription(){
        stream.subscribeCandles(companies.getFigisOfTradingCompanies(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
    }

    @Override
    public Queue<HistoricCandle> uploadCandles(String figi, CandleInterval candleInterval, int candleStepsBack) {
        int sec = secondsInCandleInterval(candleInterval);

        return new LinkedList<HistoricCandle>(api.getMarketDataService().getCandlesSync(figi,
                Instant.now().minusSeconds((long) sec * candleStepsBack),
                Instant.now(),
                candleInterval));
    }//todo: протестить

    private int secondsInCandleInterval(CandleInterval candleInterval){
        switch(candleInterval){
            case CANDLE_INTERVAL_1_MIN:
                return 60;
            case CANDLE_INTERVAL_5_MIN:
                return 60 * 5;
            case CANDLE_INTERVAL_15_MIN:
                return 60 * 15;
            case CANDLE_INTERVAL_HOUR:
                return 60 * 60;
            case CANDLE_INTERVAL_DAY:
                return 60 * 60 * 24;
            default:
                return 60;
        }
    }

    public void printQueue(Queue<HistoricCandle> uploadCandles){
        for(HistoricCandle h : uploadCandles){
            System.out.println(h.toString());
        }
    }
}
