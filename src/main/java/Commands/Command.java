package Commands;

import Exceptions.CommandException;

public interface Command {
    String getDescription();
    String getName();
    boolean execute(String argument) throws CommandException;
}
