package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;

public class StartTradeCommand extends AbstractCommand {
    private CompanyCollection companies;
    private CandleStream stream;

    public StartTradeCommand(CompanyCollection companies, CandleStream stream) {
        super("star-trade {figi}", "Start trade company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if(argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

        if(!companies.isContainsFigi(argument)) throw new IllegalCommandArgsException("Illegal Number of args");

        try {
            companies.getByFigi(argument).startTrade(stream);
            stream.updateSubscription();
        } catch (CompanyNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
