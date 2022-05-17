package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;

public class StopTrade implements Command{
    CompanyCollection companies;
    CandleStream stream;

    public StopTrade(CompanyCollection companies, CandleStream stream){
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(Commands.CommandType.DELETE).get();
        if(args.size() != 1) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = args.get(0);
        if(companies.isContainsFigi(figi)) {
            try {
                companies.getByFigi(figi).tradeOff();
                stream.updateSubscription();
            } catch (CompanyNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
