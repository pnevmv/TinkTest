package Connection;
import Data.*;
import Proccesor.StreamProcessor;
import com.google.protobuf.Timestamp;
import com.google.type.DateTime;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.MarketDataService;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;


import java.util.*;
import java.util.function.Consumer;


public class CandleStream implements CandleSource{
    private CompaniesForTrading companies;
    private MarketDataStreamService marketStreamServ;
    private MarketDataService marketServ;
    private MarketDataSubscriptionService stream;

    private int connectionAttempts;

    { connectionAttempts = 0; }
    public CandleStream(InvestApi api, CompaniesForTrading companies){
        marketStreamServ = api.getMarketDataStreamService();
        this.companies = companies;
    }

    public void inicialize(StreamProcessor processor) throws OutNumberOfReconnectAttemptsException,  CompanyNotFoundException {
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
    public Queue<Candle> uploadCandles(String figi, CandleInterval candleInterval, Timestamp fromDate) {
       // marketServ.getCandles(figi, )
        return null;
    }
}
