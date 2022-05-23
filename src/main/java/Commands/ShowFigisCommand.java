package Commands;

import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * Class command for showing
 * all figis of companies in collection to trade
 */
public class ShowFigisCommand extends AbstractCommand {
    private final CompanyCollection companyCollection;

    public ShowFigisCommand(CompanyCollection companyCollection) {
        super("show-figis", "display figi of each company");
        this.companyCollection = companyCollection;
    }

    /**
     * Executes the command
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new IllegalCommandArgsException();
            for (String figi: companyCollection.getFigis()) {
                Console.println(figi);
            }
        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}
