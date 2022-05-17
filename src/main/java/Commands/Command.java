package Commands;

import Exceptions.CommandException;

public interface Command {
    public void execute(CommandArgsSource argsSource) throws CommandException;
}
