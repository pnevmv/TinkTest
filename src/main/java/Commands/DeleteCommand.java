package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class DeleteCommand  extends AbstractCommand {
    private String name;
    private String description;
    private final CompanyCollection companies;
    private final CandleStream stream;

    public DeleteCommand(CompanyCollection companies, CandleStream stream){
        super("delete {figi}", "deletes company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if(argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = argument;
        if(companies.isContainsFigi(figi)) companies.removeByFigi(figi);
        stream.updateSubscription();
        return true;
    }
}
