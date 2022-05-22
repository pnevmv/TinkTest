package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;
import Exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;

public class AddCompanyCommand extends AbstractCommand{
    private Connector connector;

    public AddCompanyCommand(Connector connector) {
        super("add {figi}", "Add company by figi with your parameters");
        this.connector = connector;
    }

    /**
     * @param argument - figi, moneyToTrade, lossPercent, takeProfit
     */
    @Override
    public boolean execute(String argument) throws CommandException {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

            return true;
    }
}
