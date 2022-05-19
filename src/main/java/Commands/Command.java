package Commands;

import Exceptions.CommandException;

public interface Command {
    void execute(CommandArgsSource argsSource) throws CommandException;
}
