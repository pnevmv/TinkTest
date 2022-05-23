package Connection;
import Data.*;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

/**
 * Class-wrapper for stream of candles
 */
public class CandleStream implements CandleSource{
    private final CompanyCollection companies;
    private final MarketDataStreamService marketStreamServ;
    private MarketDataSubscriptionService stream;
    private final InvestApi api;

    public CandleStream(InvestApi api, CompanyCollection companies){
        this.api = api;
        this.marketStreamServ = api.getMarketDataStreamService();
        this.companies = companies;
    }

    /**
     * Creates stream
     * @param processor
     * @throws OutNumberOfReconnectAttemptsException
     * @throws CompanyNotFoundException
     */
    public void initialize(DataStreamProcessor processor) throws OutNumberOfReconnectAttemptsException, CompanyNotFoundException {

        Consumer<Throwable> streamError = e -> {
            System.out.println("something is wrong");
            System.out.println(e.toString());
            e.printStackTrace();
        };

        stream = marketStreamServ.newStream("Candles", processor::process, streamError);
        if (!companies.getFigisOfTradingCompanies().isEmpty()) stream.subscribeCandles(companies.getFigisOfTradingCompanies(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
    }

    /**
     * Updates subscription
     * Call every time you change some company isTrades flag, or delete company!
     */
    public void updateSubscription() {
        if(!companies.getFigisOfTradingCompanies().isEmpty()) {
            System.out.println(companies.getFigisOfTradingCompanies().get(0));
            stream.subscribeCandles(companies.getFigisOfTradingCompanies(), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
        }
    }

/**
 * Class stream of source of candles from market data service
 */
    @Override
    public Queue<HistoricCandle> uploadCandles(String figi, CandleInterval candleInterval, int candleStepsBack) {
        return new LinkedList<>(api.getMarketDataService().getCandlesSync(figi,
                Instant.now().minusSeconds((long) secondsInCandleInterval(candleInterval) * candleStepsBack),
                Instant.now(),
                candleInterval));
    }

    /**
     * Calculate how many seconds in every candle Interval
     * @param candleInterval
     * @return econds in every candle Interval
     */
    private int secondsInCandleInterval(CandleInterval candleInterval){
        switch(candleInterval){
            case CANDLE_INTERVAL_DAY:
                return 60 * 60 * 24;
            case CANDLE_INTERVAL_HOUR:
                return 60 * 60;
            case CANDLE_INTERVAL_5_MIN:
                return 60 * 5;
            case CANDLE_INTERVAL_15_MIN:
                return 60 * 15;
            default:
                return 60;
        }
    }

}
