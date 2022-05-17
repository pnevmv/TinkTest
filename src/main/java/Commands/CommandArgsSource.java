package Commands;

import Exceptions.IllegalCommandArgsException;
import Exceptions.NoSuchCommandException;

import java.util.List;
import java.util.function.Supplier;

public interface CommandArgsSource {
    Supplier<List<String>> getArgsByCommand(Commands.Commands.CommandType commandType) throws IllegalCommandArgsException, NoSuchCommandException;
}
