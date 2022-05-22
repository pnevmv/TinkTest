package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;

public class PrintScheduleForThisDayCommand extends AbstractCommand {
    private Connector connector;

    public PrintScheduleForThisDayCommand(Connector connector) {
        super("print-schedule-today", "Display schedule for this day");
        this.connector = connector;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        if (argument.isEmpty()) throw new IllegalCommandArgsException("Illegal Number of args");

        connector.printScheduleForThisDay();
        return true;
    }
}