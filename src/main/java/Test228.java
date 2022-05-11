import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test228 {
    static final Logger log = LoggerFactory.getLogger(Test228.class);
    public static void main(String[] args) {
        var token = "t.HEtLJq48JSgIiS9Yjy6ZOvjQbtO7NBt-M1mVSOhj0rUN32xrTtfzCzlH3ikjGGCqHs2v0zasLonfsRLWvw4NiQ";
        var api = InvestApi.create(token);

        //instrumentsServiceExample(api);
        //accInfo(api);
        candles(api);
    }
    private  static void candles(InvestApi api){
        var instr = api.getInstrumentsService();
        var serv = api.getMarketDataService();

        try {
            String tatneftFigi = "BBG004S68829";
            var tatneft = instr.getShareByFigi(tatneftFigi).get();
            LocalDateTime start = LocalDateTime.now().minusDays(5);
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

    private  static void accInfo(InvestApi api){

        try {
            var acc = api.getUserService().getAccounts().get().get(0);
            var accServ = api.getOperationsService();

            var dhdhdh = accServ.getPortfolio(acc.getId());
            var m = dhdhdh.get();
            System.out.println(m.getTotalAmountEtfs().getValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
