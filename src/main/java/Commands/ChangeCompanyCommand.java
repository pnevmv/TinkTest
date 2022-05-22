package Commands;


import Connection.Connector;
import Data.Company;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.math.BigDecimal;

/**
 * User can change amount of money that bot can use for trading stocks of this company
 */
public class ChangeCompanyCommand extends AbstractCommand{
    private final CompanyBuilder companyBuilder;
    private final CompanyCollection companyCollection;
    private final Connector connector;

    public ChangeCompanyCommand(CompanyBuilder companyBuilder, CompanyCollection companyCollection, Connector connector) {
        super("change-money {figi}", "Change money allowed for company");
        this.companyBuilder = companyBuilder;
        this.companyCollection = companyCollection;
        this.connector = connector;
    }

    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in the wrong format!");
            companyCollection.removeByFigi(argument);
            double moneyToTrade = companyBuilder.askMoneyToTrade();
            if (BigDecimal.valueOf(moneyToTrade).compareTo(connector.getAmountOfMoney()) > 0) throw new IllegalCommandArgsException("Wrong value of money");
            double lossPercent = companyBuilder.askLossPercent();
            double takeProfit = companyBuilder.askTakeProfit();
            Company newCompany = new Company(argument, moneyToTrade, lossPercent, takeProfit, 2);
            companyCollection.putCompanyByFigi(argument, newCompany);

        } catch (IllegalCommandArgsException exception) {
            Console.printError(exception.getMessage());
        }
        return true;
    }
}
