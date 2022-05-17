package Commands;

import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class Delete implements Command{
    CompanyCollection companies;

    public Delete(CompanyCollection companies){
        this.companies = companies;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(Commands.CommandType.DELETE).get();
        if(args.size() != 1) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = args.get(0);
        if(companies.isContainsFigi(figi)) companies.removeByFigi(figi);
    }
}
