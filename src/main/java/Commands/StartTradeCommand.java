package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * Class command for starting
 * trade company by figi in collection to trade
 */
public class StartTradeCommand extends AbstractCommand {
    private final CompanyCollection companies;
    private final CandleStream stream;

    public StartTradeCommand(CompanyCollection companies, CandleStream stream) {
        super("start-trade {figi}", "Start trade company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    /**
     * Executes the command
     * @param argument - figi of company
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in wrong format!");
            if (!companies.isContainsFigi(argument)) throw new CompanyNotFoundException("There's no company with figi" + argument);

            companies.getByFigi(argument).startTrade(stream);
            stream.updateSubscription();
        } catch (IllegalCommandArgsException | CompanyNotFoundException exception) {
            Console.printError(exception.getMessage());
        }
        return true;
    }
}
