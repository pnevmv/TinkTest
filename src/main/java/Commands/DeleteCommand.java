package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class DeleteCommand implements Command {
    private CompanyCollection companies;
    private CandleStream stream;

    public DeleteCommand(CompanyCollection companies, CandleStream stream){
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(CommandType.DELETE).get();
        if(args.size() != 1) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = args.get(0);
        if(companies.isContainsFigi(figi)) companies.removeByFigi(figi);
        stream.updateSubscription();
    }
}
