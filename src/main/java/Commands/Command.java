package Commands;

import Exceptions.CommandException;

/**
 * Interface command for all commands
 */
public interface Command {

    /**
     * @return format of command (name + {argument})
     */
    String getName();

    /**
     * @return description of command and examples
     */
    String getDescription();

    /**
     * @param argument of command if it's necessary
     * @return success/fail execution
     */
    boolean execute(String argument) throws CommandException;
}
