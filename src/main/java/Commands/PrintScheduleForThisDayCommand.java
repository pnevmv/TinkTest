package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class PrintScheduleForThisDayCommand implements Command{
    private Connector connector;

    public PrintScheduleForThisDayCommand(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(CommandType.DELETE).get();
        if(args.size() != 0) throw new IllegalCommandArgsException("Illegal Number of args");

        connector.printScheduleForThisDay();
    }
}