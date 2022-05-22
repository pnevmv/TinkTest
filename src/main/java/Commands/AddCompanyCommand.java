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
        super("add {figi} {money to trade} {loss percent} {take profit}", "adds company by figi with your parameters");
        this.connector = connector;
    }

    /**
     * @param argument - figi, moneyToTrade, lossPercent, takeProfit
     */
    @Override
    public boolean execute(String argument) throws CommandException {
            if (argument.length() != 4) throw new IllegalCommandArgsException("Illegal Number of args");
            /*if (!connector.isExistByFigi(argument[0])) throw new CompanyNotFoundException("Invalid figi");
            if (BigDecimal.valueOf(Integer.parseInt(argument[1])).compareTo(connector.getAmountOfMoney()) > 0) throw new NotEnoughMoneyException("You don't have enough money");
            if (Integer.parseInt(argument[2]) > 100 && Integer.parseInt(argument[2]) < 0)  throw new IllegalCommandArgsException("Invalid stop-loss (> 100 | < 0)");
            if (Integer.parseInt(argument[3]) < 0) throw new IllegalCommandArgsException("Invalid take-profit (< 0)");
            */
            return true;
    }
}
