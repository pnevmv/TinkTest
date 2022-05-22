package Commands;

import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

public class ShowFigisCommand extends AbstractCommand {
    private CompanyCollection companyCollection;

    public ShowFigisCommand(CompanyCollection companyCollection) {
        super("show-figis", "display figi of each company");
        this.companyCollection = companyCollection;
    }

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
