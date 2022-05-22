package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

public class PrintScheduleForThisDayCommand extends AbstractCommand {
    private Connector connector;

    public PrintScheduleForThisDayCommand(Connector connector) {
        super("print-schedule-today", "Display schedule for this day");
        this.connector = connector;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (!argument.isEmpty()) throw new IllegalCommandArgsException();
            connector.printScheduleForThisDay();
        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}