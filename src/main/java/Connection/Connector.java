package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.StreamProcessor;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.core.InvestApi;
import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    public void printSchedule(InvestApi api) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));

        for (TradingDay tradingDay : tradingSchedules.getDaysList()) {
            var date = timestampToString(tradingDay.getDate());
            var startDate = timestampToString(tradingDay.getStartTime());
            var endDate = timestampToString(tradingDay.getEndTime());
            if (tradingDay.getIsTradingDay()) {
                System.out.printf("Расписание торгов для площадки MOEX. Дата: {%s},  открытие: {%s}, закрытие: {%s}\n", date, startDate, endDate);
            } else {
                System.out.printf("Расписание торгов для площадки MOEX. Дата: {%s}. Выходной день\n", date);
            }

        }
    }

    public boolean isAvailableNow(InvestApi api) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));


        var today = tradingSchedules.getDays(0);
        if (today.getIsTradingDay()) {

            var startDate = timestampToString(today.getStartTime());
            System.out.println(today.getStartTime() + "\nds " + today.getEndTime());
            var endDate = timestampToString(today.getEndTime());
        }
        return today.getIsTradingDay();
    }

    public void printScheduleForThisDay(InvestApi api) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        var startDate = timestampToString(today.getStartTime());
        var endDate = timestampToString(today.getEndTime());
        if (today.getIsTradingDay()) {
            System.out.printf("Расписание торгов для площадки MOEX на сегодня. Открытие: {%s}, закрытие: {%s}\n", startDate, endDate);
        } else {
            System.out.printf("Расписание торгов для площадки MOEX. Дата: {%s}. Выходной день\n");
        }
    }
}

/*
-проверка работоспособности биржи
-верификации компании по фиги
-получение портфолио
-инициализация стримов свеч и торговли
-обработка исключений
*/
