package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.IllegalCommandArgsException;
import Exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;

public class AddCompany implements Command{
    private Connector connector;

    public AddCompany(Connector connector) {
        this.connector = connector;
    }

    /**
     * @param argsSource - figi, moneyToTrade, lossPercent, takeProfit
     */
    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
            var getArgs = argsSource.getArgsByCommand(CommandType.ADD);
            var args = getArgs.get();

            if (args.size() != 4) throw new IllegalCommandArgsException("Illegal Number of args");
            if (!connector.isExistByFigi(args.get(0))) throw new CompanyNotFoundException("Invalid figi");
            if (BigDecimal.valueOf(Integer.parseInt(args.get(1))).compareTo(connector.getAmountOfMoney()) > 0) throw new NotEnoughMoneyException("You don't have enough money");
            if (Integer.parseInt(args.get(2)) > 100 && Integer.parseInt(args.get(2)) < 0)  throw new IllegalCommandArgsException("Invalid stop-loss (> 100 | < 0)");
            if (Integer.parseInt(args.get(3)) < 0) throw new IllegalCommandArgsException("Invalid take-profit (< 0)");
    }
}
