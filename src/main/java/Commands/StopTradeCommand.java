package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

public class StopTradeCommand extends AbstractCommand {
    private final CompanyCollection companies;
    private final CandleStream stream;

    public StopTradeCommand(CompanyCollection companies, CandleStream stream) {
        super("stop-trade {figi}", "Stop trading for company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in wrong format!");
            companies.stopTradingByFigi(argument, stream);
        } catch (IllegalCommandArgsException exception) {
            Console.printError(exception.getMessage());
        }
        return true;
    }
}
