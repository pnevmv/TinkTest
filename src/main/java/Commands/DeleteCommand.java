package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import UI.Console.Console;

public class DeleteCommand  extends AbstractCommand {
    private final CompanyCollection companies;
    private final CandleStream stream;

    public DeleteCommand(CompanyCollection companies, CandleStream stream){
        super("delete {figi}", "Delete company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalArgumentException();
            if(companies.isContainsFigi(argument)) companies.removeByFigi(argument);
            stream.updateSubscription();
        } catch (IllegalArgumentException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}
