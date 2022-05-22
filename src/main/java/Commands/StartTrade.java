package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;

public class StartTrade implements Command{
    private CompanyCollection companies;
    private CandleStream stream;

    public StartTrade(CompanyCollection companies, CandleStream stream){
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(CommandType.START).get();
        if(args.size() != 1) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = args.get(0);
        if(!companies.isContainsFigi(figi)) throw new IllegalCommandArgsException("Illegal Number of args");

        try {
            companies.getByFigi(figi).startTrade(stream);
            stream.updateSubscription();
        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }
    }
}
