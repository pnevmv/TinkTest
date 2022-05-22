package Commands;

import Connection.Connector;
import Data.Company;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.math.BigDecimal;

public class AddCompanyCommand extends AbstractCommand{
    private final CompanyBuilder companyBuilder;
    private final CompanyCollection companyCollection;
    private final Connector connector;

    public AddCompanyCommand(CompanyBuilder companyBuilder, CompanyCollection companyCollection, Connector connector) {
        super("add {figi}", "Add company by figi with your parameters");
        this.companyBuilder = companyBuilder;
        this.companyCollection = companyCollection;
        this.connector = connector;
    }

    /**
     * @param argument - figi, moneyToTrade, lossPercent, takeProfit
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in the wrong format!");
            double lossPercent = companyBuilder.askLossPercent();
            double takeProfit = companyBuilder.askTakeProfit();double moneyToTrade = companyBuilder.askMoneyToTrade();
            if (BigDecimal.valueOf(moneyToTrade).compareTo(connector.getAmountOfMoney()) > 0) throw new IllegalCommandArgsException("Wrong value of money");
            Company company = new Company(argument, moneyToTrade, lossPercent, takeProfit, 2);
            companyCollection.putCompanyByFigi(argument, company);
        } catch (IllegalCommandArgsException exception) {
            Console.printError(exception.getMessage());
        }

        return true;
    }
}
