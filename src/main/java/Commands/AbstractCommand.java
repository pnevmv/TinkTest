package Commands;

import Exceptions.CommandException;

/**
 * Abstract class for all command
 */
public class AbstractCommand implements Command {
    private final String name;
    private final String description;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Name and usage way of the command.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param argument of command if it's necessary
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        return true;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
