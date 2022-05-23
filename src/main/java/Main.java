import Commands.*;
import Connection.Connector;
import Connection.TradeStream;
import Data.*;
import Exceptions.AccountNotFoundException;
import Exceptions.CommandException;
import Proccesor.DataStreamProcessor;
import Proccesor.MoneyQuotationProcessor;
import Proccesor.Trader;
import UI.Console.Console;
import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    static final String appName = "ChnrVn";
    //"t.HEtLJq48JSgIiS9Yjy6ZOvjQbtO7NBt-M1mVSOhj0rUN32xrTtfzCzlH3ikjGGCqHs2v0zasLonfsRLWvw4NiQ"

    public static void main(String[] args) {

        try (Scanner userScanner = new Scanner(System.in)) {

            InvestApi api = initializeApi(userScanner);
            String accountId = chooseAccount(api, userScanner);

            CompanyCollection companyCollection = new CompanyCollection();
            Connector connector = new Connector(api, companyCollection, accountId);
            Trader trader = new Trader(connector, api);
            DataStreamProcessor dataProc = new DataStreamProcessor(companyCollection, trader);

            connector.initializeStreams(dataProc);

           // testRealTrade( connector,  companyCollection,  api, connector.getTradeStream());
            CompanyBuilder companyBuilder = new CompanyBuilder(userScanner);
            CommandManager commandManager = new CommandManager(
                    new HelpCommand(),
                    new AddCompanyCommand(companyBuilder, companyCollection, connector),
                    new ChangeCompanyCommand(companyBuilder, companyCollection, connector),
                    new DeleteCommand(companyCollection, connector.getCandleStream()),
                    new PrintScheduleCommand(connector),
                    new PrintScheduleForThisDayCommand(connector),
                    new ShowCommand(companyCollection),
                    new ShowFigisCommand(companyCollection),
                    new StartAllCommand(companyCollection, connector.getCandleStream()),
                    new StartTradeCommand(companyCollection, connector.getCandleStream()),
                    new StopAllCommand(companyCollection, connector.getCandleStream()),
                    new StopTradeCommand(companyCollection, connector.getCandleStream()));

            Console console = new Console(commandManager, userScanner);
            console.interactiveMode();
        } catch (CommandException exception) {
            Console.printError(exception.getMessage());
        } catch (ApiRuntimeException exception) {
            Console.printError("Internal critical error, try restart");
            exception.printStackTrace();
        }
    }

    private static InvestApi initializeApi(Scanner userScanner) {
        InvestApi api;
        String token;

        while (true) {
            try {
                Console.println("Input your token:");
                Console.print("> ");
                token = userScanner.nextLine().trim();
                if (token.isEmpty()) throw new IllegalArgumentException();

                api = InvestApi.create(token, appName);
                break;
            } catch (IllegalArgumentException exception) {
                Console.printError("Invalid token!");
            } catch (ApiRuntimeException exception) {
                Console.printError("Can't create API with this token, try again");
            }
        }

        return api;
    }

    private static String chooseAccount(InvestApi api, Scanner userScanner) {
        List<Account> accounts = api.getUserService().getAccountsSync();
        List<Account> tinkoffAccounts = new ArrayList<>();
        String accountId = "";
        int userInput;
        int number = 1;
        try {
            for (Account account: accounts) {
                if (account.getType() == AccountType.ACCOUNT_TYPE_TINKOFF) {
                    tinkoffAccounts.add(account);
                    Console.println("Account #" + number++
                            + "\nName: " + account.getName()
                            + "\nId: " + account.getId());
                }
            }
            if (tinkoffAccounts.isEmpty()) throw new AccountNotFoundException("Accounts not found");

            while (true) {
                try {
                    Console.println("Account choosing (type number from 1)");
                    Console.print("> ");
                    userInput = Integer.parseInt(userScanner.nextLine());
                    if (userInput < 1 || userInput > tinkoffAccounts.size()) throw new IllegalArgumentException("Invalid number of account, try again");
                    accountId = tinkoffAccounts.get(userInput - 1).getId();
                    if (accountId == "") throw new AccountNotFoundException("Accounts not found");
                    break;
                } catch (IllegalArgumentException exception) {
                    Console.printError(exception.getMessage());
                }

            }
        } catch (AccountNotFoundException exception) {
            Console.printError(exception.getMessage());
        }
        Console.println("Success");
        return accountId;
    }











    // создать фейк компанию для те ста пайплайна
   /*  public static Company createFakeCompanyForBuying(){
        HashMap<IndexType, Index> companyIndex = new  HashMap<>();
        companyIndex.put(IndexType.RSI, new Index(IndexType.RSI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 14));
        companyIndex.put(IndexType.NVI, new Index(IndexType.PVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));
        companyIndex.put(IndexType.PVI, new Index(IndexType.NVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));

        OpenDeals openDeals = new OpenDeals();
        Company c = new Company(
                "BBG004730N88",
                100_000,
                100_000,
                10,
                0,
                true,
                0,
                10,
                companyIndex,
                openDeals
        );

        return c;
    }

    public static Company createFakeCompanyForSelling(InvestApi api){
        HashMap<IndexType, Index> companyIndex = new  HashMap<>();
        companyIndex.put(IndexType.RSI, new Index(IndexType.RSI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 14));
        companyIndex.put(IndexType.NVI, new Index(IndexType.PVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));
        companyIndex.put(IndexType.PVI, new Index(IndexType.NVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));

        double closePrice = MoneyQuotationProcessor.convertFromQuation(api.getMarketDataService().getCandlesSync("BBG004730N88",
                Instant.now().minusSeconds(120),
                Instant.now(),
                CandleInterval.CANDLE_INTERVAL_1_MIN
        ).get(0).getClose()).doubleValue();

        OpenDeals openDeals = new OpenDeals(List.of(
                new Deal(100, BigDecimal.valueOf(closePrice * (1.01)), BigDecimal.valueOf(closePrice * (1.01) * 0.90), "0"),
                new Deal(100, BigDecimal.valueOf(closePrice * (1.05)), BigDecimal.valueOf(closePrice * (1.05) * 0.90), "1"),
                new Deal(100, BigDecimal.valueOf(closePrice * (1.1)), BigDecimal.valueOf(closePrice * (1.1) * 0.90), "2"),
                new Deal(100, BigDecimal.valueOf(closePrice * (0.9)), BigDecimal.valueOf(closePrice * (0.9) * 0.90), "3"), //явно тейкпрофит
                new Deal(100, BigDecimal.valueOf(closePrice * (1.00)), BigDecimal.valueOf(closePrice * (1.0) * 0.90), "4"),
                new Deal(100, BigDecimal.valueOf(closePrice * (0.8)), BigDecimal.valueOf(closePrice * (0.8) * 0.90), "5"),//явно тейкпрофит
                new Deal(100, BigDecimal.valueOf(closePrice * (1.8)), BigDecimal.valueOf(closePrice * (1.8) * 0.90), "6") //явно не пройдет по стоп цене
        ));

        Company c = new Company(
                "BBG004730N88",
                10000000,
                1000000,
                10,
                5,
                true,
                0,
                10,
                companyIndex,
                openDeals
        );

        return c;
    }

    public static List<TradesStreamResponse> createFakeResponse(InvestApi api){
        List<TradesStreamResponse> responses = new ArrayList<>();

        String accId = api.getUserService().getAccountsSync().get(0).getId();

        responses.add(
                TradesStreamResponse.newBuilder().setOrderTrades(
                        OrderTrades.newBuilder().setOrderId("0")
                                .setCreatedAt(Timestamp.newBuilder().getDefaultInstanceForType())
                                .setFigi("BBG004730N88")
                                .setDirectionValue(1)
                                .addTrades(
                                        OrderTrade.newBuilder()
                                                .setPrice(Quotation.newBuilder().setUnits(221).setNano(12).build())
                                                .setQuantity(3)
                                )
                                .setAccountId(accId)
                ).build()
        );
        responses.add(
                TradesStreamResponse.newBuilder().setOrderTrades(
                        OrderTrades.newBuilder().setOrderId("1")
                                .setCreatedAt(Timestamp.newBuilder().getDefaultInstanceForType())
                                .setFigi("BBG004730N88")
                                .setDirectionValue(1)
                                .addTrades(
                                        OrderTrade.newBuilder()
                                                .setPrice(Quotation.newBuilder().setUnits(233).setNano(1232).build())
                                                .setQuantity(4)
                                )
                                .setAccountId(accId)
                ).build()
        );

        responses.add(
                TradesStreamResponse.newBuilder().setOrderTrades(
                        OrderTrades.newBuilder().setOrderId("1")
                                .setCreatedAt(Timestamp.newBuilder().getDefaultInstanceForType())
                                .setFigi("BBG004730N88")
                                .setDirectionValue(2)
                                .addTrades(
                                        OrderTrade.newBuilder()
                                                .setPrice(Quotation.newBuilder().setUnits(200).setNano(1232).build())
                                                .setQuantity(3)
                                )
                                .setAccountId(accId)
                ).build()
        );

        return responses;
    }
    public static void testSubscriptionAndCalc(Connector connector, CompanyCollection companyCollection, InvestApi api){
        companyCollection.putCompanyByFigi("BBG004730N88", createFakeCompanyForSelling(api));
        companyCollection.getCompanyByFigi("BBG004730N88").startTrade(connector.getCandleStream());
        connector.getCandleStream().updateSubscription();
        while(true);
    }

    /*public static void testTradeResponseProcessing(Connector connector, CompanyCollection companyCollection
            , InvestApi api, TradeStreamProcessor t) {
        companyCollection.putCompanyByFigi("BBG004730N88", createFakeCompanyForBuying());
        companyCollection.getCompanyByFigi("BBG004730N88").startTrade(connector.getCandleStream());
        companyCollection.getCompanyByFigi("BBG004730N88").getOpenDeals().printDeals();
        for(TradesStreamResponse tr : createFakeResponse(api)) t.responseProcess(tr);
        companyCollection.getCompanyByFigi("BBG004730N88").getOpenDeals().printDeals();
        System.out.println( companyCollection.getCompanyByFigi("BBG004730N88").toString());
    }*/


    public static Company createFakeCompanyForRealTrading(){
        HashMap<IndexType, Index> companyIndex = new  HashMap<>();
        companyIndex.put(IndexType.RSI, new Index(IndexType.RSI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 14));
        companyIndex.put(IndexType.NVI, new Index(IndexType.PVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));
        companyIndex.put(IndexType.PVI, new Index(IndexType.NVI, 0, CandleInterval.CANDLE_INTERVAL_1_MIN, 1));


        OpenDeals openDeals = new OpenDeals(List.of(
                new Deal(41, BigDecimal.valueOf(4.1), BigDecimal.valueOf( 4.1 * 0.99999),
                        String.valueOf(Double.valueOf(Math.random()).hashCode()))
        ));

        Company c = new Company(
                "BBG333333333",
                89,
                89,
                2,
                0,
                true,
                5,
                1,
                companyIndex,
                openDeals
        );

        return c;
    }

    public static void testRealTrade(Connector connector, CompanyCollection companyCollection, InvestApi api, TradeStream t){
        companyCollection.putCompanyByFigi("BBG333333333", createFakeCompanyForRealTrading());
        companyCollection.getCompanyByFigi("BBG333333333").startTrade(connector.getCandleStream());
         connector.getCandleStream().updateSubscription();

       /* try {
            t.buyStock(4, api.getMarketDataService().getCandlesSync("BBG333333333",
                    Instant.now().minusSeconds(61),
                    Instant.now(),
                    CandleInterval.CANDLE_INTERVAL_1_MIN
            ).get(0).getClose(), "BBG333333333");

            Deal d = companyCollection.getCompanyByFigi("BBG333333333").getOpenDeals().getDealById("1").get();

            t.sellStock(4,  api.getMarketDataService().getCandlesSync("BBG333333333",
                    Instant.now().minusSeconds(61),
                    Instant.now(),
                    CandleInterval.CANDLE_INTERVAL_1_MIN
                    ).get(0).getClose(), "BBG333333333",
                    d
            );

        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }*/

        companyCollection.getCompanyByFigi("BBG333333333").getOpenDeals().printDeals();

        while(true);
    }


}



