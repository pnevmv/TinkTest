package Commands;

import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import Exceptions.NoSuchCommandException;

public class AddCompany implements Command{
    /**
     *
     * @param argsSource - figi, moneyToTrade, lossPercent, TakeProfit
     */
    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
            var getArgs = argsSource.getArgsByCommand(Commands.Commands.CommandType.ADD);
            var args = getArgs.get();

            if(args.size() != 4) throw new IllegalCommandArgsException("Illegal Number of args");

    }
}
