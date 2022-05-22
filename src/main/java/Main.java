import Connection.Connector;
import Data.Company;
import Data.CompanyCollection;
import Proccesor.CheckClass;
import Proccesor.DataStreamProcessor;
import Proccesor.TradeStreamProcessor;
import Proccesor.Trader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;


//todo: make all collections getters immutable
public class Main {
    static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        var token = "t.HEtLJq48JSgIiS9Yjy6ZOvjQbtO7NBt-M1mVSOhj0rUN32xrTtfzCzlH3ikjGGCqHs2v0zasLonfsRLWvw4NiQ";
        String appName = "ChnrVn";
        var api = InvestApi.create(token, appName);

        String figi = args[0];
        //System.out.println(api.getInstrumentsService().getShareByFigiSync(figi));

        CompanyCollection companies = new CompanyCollection();

        Company c = new Company(figi, 500, 101, 2, 7);
        companies.putCompanyByFigi(figi, c);

        Company t = new Company("BBG004S68829", 500, 101, 2, 7);
        companies.putCompanyByFigi("BBG004S68829", t);

        Connector connector = new Connector(api, companies);
        Trader trader = new Trader(connector, api);
        DataStreamProcessor dataProc = new DataStreamProcessor(companies, trader);
        TradeStreamProcessor tradeProc = new TradeStreamProcessor(companies);
        connector.initializeStreams(dataProc, tradeProc);

        companies.startTradingForAll(connector.getCandleStream());

        connector.getCandleStream().updateSubscription();

        long time = System.currentTimeMillis();
         while (System.currentTimeMillis() - time < 121000) { }


        /*
        test(api, figi);
        */
    }

    private static void test(InvestApi api, String figi){
        CheckClass ssc = new CheckClass();
        ssc.updateHistory(Candle.getDefaultInstance());

        long time = System.currentTimeMillis();
        StreamProcessor<MarketDataResponse> processor = response -> {
            System.out.println(response.getCandle().getTime().getNanos());
            System.out.println(System.currentTimeMillis());
            if (response.hasPing()) {
                log.info("ping");
            } else if (response.hasCandle() && response.getCandle().getTime().getSeconds()
                    != ssc.getSecsOfLAstCAndle()) {
                ssc.printquue();
                ssc.updateHistory(response.getCandle());
                log.info("New Candle Data: {}", response);
            }else if (response.hasSubscribeCandlesResponse()) {
                var successCount = response.getSubscribeCandlesResponse().getCandlesSubscriptionsList().stream().filter(el -> el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                var errorCount = response.getSubscribeTradesResponse().getTradeSubscriptionsList().stream().filter(el -> !el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                log.info("Success tries: {}", successCount);
                log.info("Not success tries: {}", errorCount);
            }
        };

        Consumer<Throwable> streamError = e -> {System.out.println(e.toString()); }; //todo: logger, correct reconnection
        api.getMarketDataStreamService().newStream("Candles", processor, streamError)
                .subscribeCandles(List.of(figi), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
        while (System.currentTimeMillis() - time < 185000) {

        }
    }

    private  static void candles(InvestApi api){
        var instr = api.getInstrumentsService();
        var serv = api.getMarketDataService();

        try {
            String tatneftFigi = "BBG004S68829";
            var tatneft = instr.getShareByFigi(tatneftFigi).get();
            LocalDateTime start = LocalDateTime.now().minusDays(9);
            LocalDateTime end = LocalDateTime.now();

            var candles= serv.getCandles("BBG004S68829", start.toInstant(ZoneOffset.ofHours(3)), end.toInstant(ZoneOffset.ofHours(3)), CandleInterval.CANDLE_INTERVAL_HOUR).get();
            for(HistoricCandle c : candles){
                Date startqq = new Date(c.getTime().getSeconds() * 1000);
                System.out.println(startqq.toString() + "   " + c.getHigh().getUnits());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    //-сравнить фримани и мани на моем аккаунте
}
