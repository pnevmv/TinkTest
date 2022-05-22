package Commands;

import UI.Console.Console;

public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "Display available commands");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new IllegalArgumentException();
        } catch (IllegalArgumentException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}
