package Connection;

import Data.CompanyCollection;
import Exceptions.AccountNotFoundException;
import Exceptions.CompanyNotFoundException;
import Exceptions.OutNumberOfReconnectAttemptsException;
import Proccesor.DataStreamProcessor;
import Proccesor.TradeStreamProcessor;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.AccountType;
import ru.tinkoff.piapi.contract.v1.TradingDay;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Portfolio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Class for unary requests (initialisations, verifications etc)
 */

public class Connector{
    private final TradeStream tradeStream;
    private final CandleStream candleStream;
    private final InvestApi api;
    private final CompanyCollection companies;

    public CompanyCollection getCompanies() {
        return companies;
    }

    public Connector(InvestApi api, CompanyCollection companies) {
        this.tradeStream = new TradeStream(api, companies);
        this.candleStream = new CandleStream(api, companies);
        this.api = api;
        this.companies = companies;
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
            e.printStackTrace();
        }

    }

    public Date timestampToDate(Timestamp timestamp) {
        return new Date(timestamp.getSeconds() * 1000);
    }

    public boolean isAvailableNow() {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        var now = System.currentTimeMillis() / 1000;
        if (today.getIsTradingDay()
                && now  >= today.getStartTime().getSeconds()
                && now < today.getEndTime().getSeconds()) {
            return true;
        }
        else return false;
    }

    public void printScheduleForThisDay() {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));

        var today = tradingSchedules.getDays(0);
        if (today.getIsTradingDay()) {
            String startTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getStartTime()));
            String endTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(today.getEndTime()));

            System.out.println("Расписание работы на сегодня:\nОткрытие: " + startTime + "\nЗакрытие: " + endTime);
        }
        else System.out.println("Сегодня биржа не работает");
    }

    public void printSchedule() {
        var tradingSchedules =
                api.getInstrumentsService().getTradingScheduleSync("moex", Instant.now(), Instant.now().plus(5, ChronoUnit.DAYS));

        for (TradingDay tradingDay : tradingSchedules.getDaysList()) {
            String date = new SimpleDateFormat("yyyy.MM.dd").format(timestampToDate(tradingDay.getDate()));
            String startTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(tradingDay.getStartTime()));
            String endTime = new SimpleDateFormat("HH.mm.ss").format(timestampToDate(tradingDay.getEndTime()));

            if (tradingDay.getIsTradingDay()) {
                System.out.printf("Расписание торгов для площадки MOEX. Дата: {%s},  открытие: {%s}, закрытие: {%s}\n", date, startTime, endTime);
            } else {
                System.out.printf("Расписание торгов для площадки MOEX. Дата: {%s}. Выходной день\n", date);
            }

        }
    }



    public Portfolio getPortfolio(String accountId) {
        return api.getOperationsService().getPortfolioSync(accountId);
    }

    public String findAccount() {
        List<Account> accounts = api.getUserService().getAccountsSync();
        String id = null;
        for (Account account: accounts) {
            if (account.getType() == AccountType.ACCOUNT_TYPE_TINKOFF) id = account.getId();
        }
        try {
            if (id == null) throw new AccountNotFoundException();
        } catch (AccountNotFoundException e) {
            System.out.println("Аккаунт не найден");
        }
        return id;
    }

    public BigDecimal getAmountOfMoney() {
        return getPortfolio(findAccount()).getTotalAmountCurrencies().getValue();
    }

}

