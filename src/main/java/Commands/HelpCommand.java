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
            return true;

        } catch (IllegalArgumentException exception) {
            Console.println("Executing the: '" + getName() + "', but illegal number of arguments");
        }
        return false;
    }
}
