package Connection;

import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import Proccesor.TradeStreamProcessor;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Portfolio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Class for unary requests (initialisations, verifications etc)
 */

public class Connector{
    private final TradeStream tradeStream;
    private final CandleStream candleStream;
    private final InvestApi api;
    private final CompanyCollection companies;
    private final String accountId;

    public CompanyCollection getCompanies() {
        return companies;
    }

    public Connector(InvestApi api, CompanyCollection companies, String accountId) {
        this.tradeStream = new TradeStream(api, accountId);
        this.candleStream = new CandleStream(api, companies);
        this.api = api;
        this.companies = companies;
        this.accountId = accountId;
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

    public void initializeStreams( DataStreamProcessor dataProc, TradeStreamProcessor tradeProc) {
        try {
            tradeStream.initialize(tradeProc);
            candleStream.initialize(dataProc);
        } catch (OutNumberOfReconnectAttemptsException e) {
            e.printStackTrace();
        } catch (CompanyNotFoundException e) {
            System.out.println("Please input again:");
            e.printStackTrace();
        }
    }

    public Date timestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getSeconds() * 1000);
    }

    public boolean isAvailableNow(String name) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync(name, Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        var now = System.currentTimeMillis() / 1000;

        return !today.getIsTradingDay()
                || now < today.getStartTime().getSeconds()
                || now >= today.getEndTime().getSeconds();
    }

    public void printScheduleForThisDay(String name) {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync(name, Instant.now(), Instant.now().plus(6, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        if (today.getIsTradingDay()) {
            String startTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getStartTime()));
            String endTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getEndTime()));

            System.out.println("Schedule for today (" + name + "):\nOpening: " + startTime + "\nClosing: " + endTime);
        }
        else System.out.println("The exchange is closed today");
    }

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

    public Portfolio getPortfolio(String accountId) {
        return api.getOperationsService().getPortfolioSync(accountId);
    }

    public BigDecimal getAmountOfMoney() {
        return getPortfolio(getAccountId()).getTotalAmountCurrencies().getValue();
    }

    public boolean isExistByFigi(String figi) {
        Share share;
        share = api.getInstrumentsService().getShareByFigiSync(figi);
        return !share.isInitialized();
    }

    public int getLotByFigi(String figi){
        return api.getInstrumentsService().getShareByFigiSync(figi).getLot();
    }
}

