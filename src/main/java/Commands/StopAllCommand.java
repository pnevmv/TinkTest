package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * Class command for stopping
 * all companies in collection to trade
 */
public class StopAllCommand extends AbstractCommand {
    private final CompanyCollection companies;
    private final CandleStream stream;

    public StopAllCommand(CompanyCollection companies, CandleStream stream){
        super("stop-all", "Finish all trade-processes");
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
            companies.stopTradingForAll(stream);
        } catch (IllegalCommandArgsException exception) {
            Console.printError(exception.getMessage());
        }
        return true;
    }
}
