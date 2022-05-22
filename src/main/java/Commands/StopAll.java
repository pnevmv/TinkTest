package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class StopAll implements Command{
    private CompanyCollection companies;
    private CandleStream stream;

    public StopAll(CompanyCollection companies, CandleStream stream){
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(CommandType.STOPALL).get();
        if(args.size() != 0) throw new IllegalCommandArgsException("Illegal Number of args");
        companies.stopTradingForAll(stream);

    }
}
