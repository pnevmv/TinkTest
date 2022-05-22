package Commands;

import Connection.CandleStream;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class StopTrade extends AbstractCommand {
    private CompanyCollection companies;
    private CandleStream stream;

    public StopTrade(CompanyCollection companies, CandleStream stream) {
        super("stop-trade {figi}", "stops trading for company by figi");
        this.companies = companies;
        this.stream = stream;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if(argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

        String figi = argument;
        if (companies.isContainsFigi(figi)) {
            companies.stopTradingByFigi(figi, stream);
        }
        return true;
    }
}
