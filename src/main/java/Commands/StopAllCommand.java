package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class StopAllCommand extends AbstractCommand {
    private CompanyCollection companies;
    private CandleStream stream;

    public StopAllCommand(CompanyCollection companies, CandleStream stream){
        super("stop-all", "Finish all trade-processes");
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
