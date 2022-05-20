package Connection;
import Data.*;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.MarketDataService;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;


public class CandleStream implements CandleSource{
    private CompanyCollection companies;
    private MarketDataStreamService marketStreamServ;
    private MarketDataService marketServ;
    private MarketDataSubscriptionService stream;
    private InvestApi api;


    private int connectionAttempts;

    { connectionAttempts = 0; }
    public CandleStream(InvestApi api, CompanyCollection companies){
        marketStreamServ = api.getMarketDataStreamService();
        this.companies = companies;
        this.api = api;
    }

    public void initialize(DataStreamProcessor processor) throws OutNumberOfReconnectAttemptsException, CompanyNotFoundException {
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
        Queue <HistoricCandle> q = new LinkedList<>();
        for (HistoricCandle histCandle : api.getMarketDataService().getCandlesSync(figi,
                Instant.now().minusSeconds(60 * candleStepsBack),
                Instant.now(),
                candleInterval)) {
            q.add(histCandle);
        }
        return q;
    }//todo: ??????????


}
