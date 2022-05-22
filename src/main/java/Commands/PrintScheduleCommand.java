package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

public class PrintScheduleCommand  extends AbstractCommand {
    private final Connector connector;

    public PrintScheduleCommand (Connector connector) {
        super("print-schedule {name of exchange}", "Display schedule of exchange (example: moex, spb)");
        this.connector = connector;
    }

    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException();
            connector.printSchedule(argument);
        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}
