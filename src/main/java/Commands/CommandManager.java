package Commands;

import UI.Console.Console;

import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager(Command... commandsInput) {
        for (Command command : commandsInput) {
            commands.put(command.getName(), command);
        }
    }

    /**
     * Prints that command is not found.
     * @param command Command, which is not found.
     * @return Command exit status.
     */
    public boolean noSuchCommand(String command) {
        Console.println("Command '" + command + "' not found. Type 'help' for help.");
        return false;
    }

    /**
     * Prints info about the all commands.
     * @param argument It's argument.
     * @return Command exit status.
     */
    public boolean helpCommand(String argument) {
        HelpCommand helpCommand = new HelpCommand();
        if (helpCommand.execute(argument)) {
            for (Command command: commands.values()) {
                Console.printable(command.getName(), command.getDescription());
            }
            return true;
        }
        else return false;
    }

    public Set<Command> getCommands() {
        return (Set<Command>) this.commands.values();
    }

    @Override
    public String toString() {
        return "CommandManager (helper class for working with commands)";
    }
}
