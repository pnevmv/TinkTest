package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class StopAll extends AbstractCommand {
    private CompanyCollection companies;
    private CandleStream stream;

    public StopAll(String name, String description, CompanyCollection companies, CandleStream stream){
        super("stop-all", "finishes all trade-processes");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if(argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");
        companies.stopTradingForAll(stream);

        return true;
    }
}
