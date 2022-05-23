package Commands;

import UI.Console.Console;

/**
 * Class command for displaying
 * all commands and their description
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "Display available commands");
    }

    /**
     * Executes the command
     * @return success/fail execution
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
