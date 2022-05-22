package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class PrintScheduleCommand  extends AbstractCommand {
    private final Connector connector;

    public PrintScheduleCommand (Connector connector) {
        super("print-schedule", "prints schedule of MOEX");
        this.connector = connector;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if(argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

        connector.printSchedule();
        return true;
    }
}
