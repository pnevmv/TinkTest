package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import UI.Console.Console;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Portfolio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Class for connection with api
 * (streams initialisation, verifications, etc.)
 */
public class Connector{
    private final TradeStream tradeStream;
    private final CandleStream candleStream;
    private final InvestApi api;
    private final CompanyCollection companies;
    private final String accountId;

    public Connector(InvestApi api, CompanyCollection companies, String accountId) {
        this.tradeStream = new TradeStream(api, accountId, companies);
        this.candleStream = new CandleStream(api, companies);
        this.api = api;
        this.companies = companies;
        this.accountId = accountId;
    }

    /**
     * Initializes candle stream
     * @param dataProc - processor - how to communicate with stream of data
     */
    public void initializeStreams(DataStreamProcessor dataProc) {
        try {
            candleStream.initialize(dataProc);
        } catch (OutNumberOfReconnectAttemptsException exception) {
            exception.printStackTrace();
        } catch (CompanyNotFoundException exception) {
            Console.println("Please input again:");
        }
    }

    /**
     * Converts from Timestamp to Date
     * @param timestamp
     * @return converted data
     */
    public Date timestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getSeconds() * 1000);
    }

    /**
     * Availability check
     * @param name of exchange
     * @return
     */
    public boolean isAvailableNow(String name) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync(name, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        var now = System.currentTimeMillis() / 1000;

        return !today.getIsTradingDay()
                || now < today.getStartTime().getSeconds()
                || now >= today.getEndTime().getSeconds();
    }

    /**
     * Prints schedule for one day
     * @param name of exchange
     */
    public void printScheduleForThisDay(String name) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync(name, Instant.now(), Instant.now().plus(6, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        if (today.getIsTradingDay()) {
            String startTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getStartTime()));
            String endTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getEndTime()));

            Console.println("Schedule for today (" + name + "):\nOpening: " + startTime + "\nClosing: " + endTime);
        }
        else Console.println("The exchange is closed today");
    }

    /**
     * Prints schedule for one week
     * @param name of exchange
     */
    public void printSchedule(String name) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync(name, Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));

        for (TradingDay tradingDay : tradingSchedules.getDaysList()) {
            String date = new SimpleDateFormat("yyyy.MM.dd").format(timestampToDate(tradingDay.getDate()));
            String startTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(tradingDay.getStartTime()));
            String endTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(tradingDay.getEndTime()));

            if (tradingDay.getIsTradingDay()) {
                System.out.printf("Schedule of working for" + name + ". Date: {%s},  opening: {%s}, closing: {%s}\n", date, startTime, endTime);
            } else {
                System.out.printf("Schedule of working for MOEX. Date: {%s}. Day off\n", date);
            }
        }
    }

    /**
     * Existing check
     * @param figi - id of instrument
     * @return
     */
    public boolean isExistByFigi(String figi) {
        var smt = api.getInstrumentsService().getInstrumentByFigiSync(figi);
        return (!smt.isInitialized());
    }

    public Portfolio getPortfolio(String accountId) {
        return api.getOperationsService().getPortfolioSync(accountId);
    }

    public BigDecimal getAmountOfMoney() {
        return getPortfolio(getAccountId()).getTotalAmountCurrencies().getValue();
    }

    public int getLotByFigi(String figi) {
        return api.getInstrumentsService().getInstrumentByFigiSync(figi).getLot();
    }

    public CompanyCollection getCompanies() {
        return companies;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public TradeStream getTradeStream() {
        return this.tradeStream;
    }

    public CandleStream getCandleStream() {
        return this.candleStream;
    }

}

