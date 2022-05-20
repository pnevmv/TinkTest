package Commands;

import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class AddCompany implements Command{
    /**
     * @param argsSource - figi, moneyToTrade, lossPercent, TakeProfit
     */
    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
            var getArgs = argsSource.getArgsByCommand(CommandType.ADD);
            var args = getArgs.get();

            if(args.size() != 4) throw new IllegalCommandArgsException("Illegal Number of args");


    }
}
