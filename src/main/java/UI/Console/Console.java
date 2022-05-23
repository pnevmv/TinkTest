package UI.Console;

import Commands.Command;
import Commands.CommandManager;
import Exceptions.CommandException;

import java.util.*;

/**
 * Class for communicating with the user
 */
public class Console {
    private final CommandManager commandManager;
    private final Scanner scanner;

    public Console(CommandManager commandManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.scanner = scanner;
    }

    /**
     * Mode for catching commands from user input.
     */
    public void interactiveMode() throws CommandException{
        Console.println("Type 'help' to display all commands");
        String[] userCommand;
        int commandStatus;
        try {
            do {
                Console.print("> ");
                userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != 1);
        } catch (NoSuchElementException exception) {
            Console.printError("No user input detected!");
        } catch (IllegalStateException exception) {
            Console.printError("Unexpected error!");
        }
    }

    /**
     * Launch the command.
     * @param userCommand Command to launch.
     * @return Exit code.
     */
    private int launchCommand(String[] userCommand) throws CommandException {
        HashMap<String, Command> commands = commandManager.getCommands();

        if (userCommand[0].equals("exit")) return 1;

        if (userCommand[0].equals("help")) {
            commandManager.helpCommand(userCommand[1]);
            return 0;
        }
        if (!commands.containsKey(userCommand[0])) {
            commandManager.noSuchCommand(userCommand[0]);
            return 0;
        }


        commands.get(userCommand[0]).execute(userCommand[1]);
        return 0;
    }

    /**
     * Prints toOut.toString() to Console
     * @param toOut Object to print
     */
    public static void print(Object toOut) {
        System.out.print(toOut);
    }

    /**
     * Prints toOut.toString() + \n to Console
     * @param toOut Object to print
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    /**
     * Prints formatted 2-element table to Console
     * @param element1 Left element of the row.
     * @param element2 Right element of the row.
     */
    public static void printable(Object element1, Object element2) {
        System.out.printf("%-37s%-1s%n", element1, element2);
    }

    /**
     * Prints error: toOut.toString() to Console
     * @param toOut Error to print
     */
    public static void printError(Object toOut) {
        System.out.println("error: " + toOut);
    }

    @Override
    public String toString() {
        return "Console - class for handling command input";
    }
}
