package Commands;

import Exceptions.CommandException;

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

    @Override
    public boolean execute(String argument) throws CommandException {
        return true;
    }

    /**
     * @return Description of the command.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
