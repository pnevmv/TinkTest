package Commands;

import Connection.Connector;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * Class command for printing working schedule of exchange for this day
 */
public class PrintScheduleForThisDayCommand extends AbstractCommand {
    private final Connector connector;

    public PrintScheduleForThisDayCommand(Connector connector) {
        super("print-schedule-today {exchange}", "Display schedule of exchange for this day (example: moex, spb)");
        this.connector = connector;
    }

    /**
     * Executes the command
     * @param argument - name of exchange
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException();
            connector.printScheduleForThisDay(argument);
        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}