import Commands.*;
import Connection.Connector;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Proccesor.DataStreamProcessor;
import Proccesor.TradeStreamProcessor;
import Proccesor.Trader;
import UI.Console.Console;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.Scanner;

public class Main {
    static final String appName = "ChnrVn";
    //"t.HEtLJq48JSgIiS9Yjy6ZOvjQbtO7NBt-M1mVSOhj0rUN32xrTtfzCzlH3ikjGGCqHs2v0zasLonfsRLWvw4NiQ"

    public static void main(String[] args) {

        try (Scanner userScanner = new Scanner(System.in)) {

            String token = initializeToken(userScanner);
            var api = InvestApi.create(token, appName);

            CompanyCollection companyCollection = new CompanyCollection();
            Connector connector = new Connector(api, companyCollection);

            Trader trader = new Trader(connector, api);
            DataStreamProcessor dataProc = new DataStreamProcessor(companyCollection, trader);
            TradeStreamProcessor tradeProc = new TradeStreamProcessor(companyCollection);
            connector.initializeStreams(dataProc, tradeProc);

            CompanyBuilder companyBuilder = new CompanyBuilder(userScanner);
            CommandManager commandManager = new CommandManager(
                    new HelpCommand(),
                    new AddCompanyCommand(companyBuilder, companyCollection),
                    new ChangeCompanyCommand(companyBuilder, companyCollection),
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
            System.out.println(exception.getMessage());
        }
    }

    private static String initializeToken(Scanner userScanner) {
        String token;

        while (true) {
            try {
                Console.println("Введите Ваш токен:");
                Console.print("> ");
                token = userScanner.nextLine().trim();
                if (token.isEmpty()) throw new IllegalArgumentException();
                break;
            } catch (IllegalArgumentException exception) {
                Console.printError("Invalid token!");
            }
        }

        return token;
    }
    //-сравнить фримани и мани на моем аккаунте
}
