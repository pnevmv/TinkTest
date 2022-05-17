import Connection.Connector;
import Data.CompanyCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;


//todo: make all collections getters immutable
public class Main {
    static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        //не песок
        var token = "t.HEtLJq48JSgIiS9Yjy6ZOvjQbtO7NBt-M1mVSOhj0rUN32xrTtfzCzlH3ikjGGCqHs2v0zasLonfsRLWvw4NiQ";
        var api = InvestApi.create(token);
        CompanyCollection companies = new CompanyCollection();
        Connector connector = new Connector(api, companies);
        connector.isAvailableNow(api);

        //песок
        //var sandboxToken = "t.iXDw7aTQ7z4uhAElj0l7V07U-65k0AevXbQ4Y9UFKxr3o8y4a4Bv4kLsfu0PVxY_vDSdVg-goXEiVq8vsGNDzw";
        //var sandboxApi = InvestApi.createSandbox(sandboxToken);
        //marketdataStreamExample(sandboxApi);

    }

    private static void marketdataStreamExample(InvestApi api) {
        var figi = List.of("BBG004730RP0");

        //Описываем, что делать с приходящими в стриме данными
        StreamProcessor<MarketDataResponse> processor = response -> {
            System.out.println("oao " + response.getCandle().getOpen());
            if (response.hasTradingStatus()) {
                System.out.println("Заебись8");
            } else if (response.hasPing()) {
                System.out.println("Заебись7");
            } else if (response.hasCandle()) {
                System.out.println("Заебись6");
            } else if (response.hasOrderbook()) {
                System.out.println("Заебись5");
            } else if (response.hasTrade()) {
                System.out.println("Заебись4");
            } else if (response.hasSubscribeCandlesResponse()) {
                var successCount = response.getSubscribeCandlesResponse().getCandlesSubscriptionsList().stream().filter(el -> el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                var errorCount = response.getSubscribeTradesResponse().getTradeSubscriptionsList().stream().filter(el -> !el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                System.out.println("Заебись3 " + successCount);
            } else if (response.hasSubscribeInfoResponse()) {
                var successCount = response.getSubscribeInfoResponse().getInfoSubscriptionsList().stream().filter(el -> el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                var errorCount = response.getSubscribeTradesResponse().getTradeSubscriptionsList().stream().filter(el -> !el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                System.out.println("Заебись2");
            } else if (response.hasSubscribeOrderBookResponse()) {
                var successCount = response.getSubscribeOrderBookResponse().getOrderBookSubscriptionsList().stream().filter(el -> el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
                System.out.println("Заебись1");
                var errorCount = response.getSubscribeTradesResponse().getTradeSubscriptionsList().stream().filter(el -> !el.getSubscriptionStatus().equals(SubscriptionStatus.SUBSCRIPTION_STATUS_SUCCESS)).count();
            }
        };
        Consumer<Throwable> onErrorCallback = error -> System.out.println(error.toString());

        api.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback).subscribeCandles(figi);

        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() - time < 1000 * 10)

        api.getMarketDataStreamService().getStreamById("candles_stream").unsubscribeCandles(figi);
    }

    private static List<String> randomFigi(InvestApi api, int count) {
        return api.getInstrumentsService().getTradableSharesSync()
                .stream()
                .filter(el -> Boolean.TRUE.equals(el.getApiTradeAvailableFlag()))
                .map(Share::getFigi)
                .limit(count)
                .collect(Collectors.toList());
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
}
