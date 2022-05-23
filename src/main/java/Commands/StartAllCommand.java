package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * Class command for starting
 * all companies in collection to trade
 */
public class StartAllCommand extends AbstractCommand {
    private final CompanyCollection companies;
    private final CandleStream stream;

    public StartAllCommand(CompanyCollection companies, CandleStream stream) {
        super("start-all", "Start trade all companies");
        this.companies = companies;
        this.stream = stream;
    }

    /**
     * Executes the command
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (!argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in wrong format!");

            companies.startTradingForAll(stream);
            stream.updateSubscription();
        } catch (IllegalCommandArgsException exception) {
            Console.printError(exception.getMessage());
        }
        return true;
    }
}
