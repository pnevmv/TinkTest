import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.MarketDataService;
import ru.tinkoff.piapi.core.stream.StreamProcessor;
import ru.tinkoff.piapi.contract.v1.SubscriptionInterval;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class CandleHandler {
    private InvestApi api;

    public CandleHandler(InvestApi api) {
        this.api = api;
    }

    public void getStream() {

        var instr = api.getInstrumentsService();
        try {
            System.out.println((instr.getShareByFigi("BBG004730RP0").get().getTradingStatus().toString()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        StreamProcessor<MarketDataResponse> processor = e -> {
            System.out.println("fgd");
            if(e.hasCandle()) System.out.println("Time" + (new Date(e.getCandle().getTime().getSeconds() * 1000).toString() )
                    + "\nOpen: " + e.getCandle().getOpen()
                    + "\nClose:" + e.getCandle().getClose());
            else System.out.println("hui");
        };

        Consumer<Throwable> onErrorCallback = error -> System.out.println(error.toString());
        api.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback).subscribeCandles(List.of("BBG004730RP0"), SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE);
    }
}
