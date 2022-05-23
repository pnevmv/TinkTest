package Data;

import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Class for communications with user
 * to get parameters of creating/changing of company
 */
public class CompanyBuilder {
    private final Scanner userScanner;

    public CompanyBuilder(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Requests allowable loss percentage
     * @return - loss percentage
     */
    public double askLossPercent() {
        double loss;

        while (true) {
            try {
                Console.println("Input loss-percent from 0 to 100:");
                Console.print("> ");
                String stringValue = userScanner.nextLine().trim();
                loss = Double.parseDouble(stringValue);

                if (loss < 0 || loss > 100) throw new IllegalCommandArgsException();
                break;
            } catch (IllegalCommandArgsException exception) {
                Console.printError("Invalid value");
            }
        }

        return loss;
    }

    /**
     * Requests allowable percentage of profit that user need
     * @return - profit percentage
     */
    public double askTakeProfit() {
        double takeProfit;

        while (true) {
            try {
                Console.println("Input take-profit:");
                Console.print("> ");
                String stringValue = userScanner.nextLine().trim();
                takeProfit = Double.parseDouble(stringValue);

                if (takeProfit < 0) throw new IllegalCommandArgsException();
                break;
            } catch (IllegalCommandArgsException exception) {
                Console.printError("Invalid value");
            }
        }

        return takeProfit;
    }

    /**
     * Requests the value of the money the user wants to allocate for trading
     * @return - profit percentage
     */
    public double askMoneyToTrade() {
        double value;

        while (true) {
            try {
                Console.println("Input value of money-to-trade:");
                Console.print("> ");
                String stringValue = userScanner.nextLine().trim();
                value = Double.parseDouble(stringValue);

                if (value < 0) throw new IllegalCommandArgsException();
                break;
            } catch (IllegalCommandArgsException exception) {
                Console.printError("Invalid value");
            }
        }

        return value;
    }

    /**
     * Requests the name of exchange
     * @return - name of exchange
     */
    public String askNameOfExchange() {
        String name;

        while (true) {
            try {
                Console.println("Input value name of exchange:");
                Console.print("> ");
                name = userScanner.nextLine().trim();

                if (name.isEmpty()) throw new IllegalCommandArgsException();
                break;
            } catch (IllegalCommandArgsException exception) {
                Console.printError("Invalid name (example: moex, spb)");
            }
        }

        return name;
    }

    /**
     * Requests the value of the money the user wants
     * to allocate for trading (with more accuracy)
     * @return - profit percentage
     */
    public BigDecimal askAllowedMoney() {
        BigDecimal value;

        while (true) {
            try {
                Console.println("Input new value of money that you want to trade:");
                Console.print("> ");
                String stringValue = userScanner.nextLine().trim();
                value = new BigDecimal(stringValue);

                if (value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalCommandArgsException();
                break;
            } catch (IllegalCommandArgsException exception) {
                Console.printError("Invalid value");
            }
        }

        return value;
    }
}
