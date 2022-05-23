package Commands;

import UI.Console.Console;

import java.util.HashMap;

/**
 * Class for managing commands
 */
public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager(Command... commandsInput) {
        for (Command command : commandsInput) {
            commands.put(command.getName().split(" ", 0)[0], command);
        }
    }

    /**
     * Prints that command is not found.
     * @param command Command, which is not found.
     */
    public void noSuchCommand(String command) {
        Console.println("Command '" + command + "' not found. Type 'help' for help.");
    }

    /**
     * Prints info about all commands.
     * @param argument - it must be empty.
     */
    public void helpCommand(String argument) {
        HelpCommand helpCommand = new HelpCommand();
        if (helpCommand.execute(argument)) {
            for (Command command: commands.values()) {
                Console.printable(command.getName(), command.getDescription());
            }
        }
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }

    @Override
    public String toString() {
        return "CommandManager (helper class for working with commands)";
    }
}
