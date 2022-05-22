package Commands;


import Data.Company;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

/**
 * User can change amount of money that bot can use for trading stocks of this company
 */
public class ChangeCompanyCommand extends AbstractCommand{
    CompanyBuilder companyBuilder;
    CompanyCollection companyCollection;

    public ChangeCompanyCommand(CompanyBuilder companyBuilder, CompanyCollection companyCollection) {
        super("change-money {figi}", "Change money allowed for company");
        this.companyBuilder = companyBuilder;
        this.companyCollection = companyCollection;
    }

    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException();
            companyCollection.removeByFigi(argument);
            double moneyToTrade = companyBuilder.askMoneyToTrade();
            double lossPercent = companyBuilder.askLossPercent();
            double takeProfit = companyBuilder.askTakeProfit();
            Company newCompany = new Company(argument, moneyToTrade, lossPercent, takeProfit, 2);
            companyCollection.putCompanyByFigi(argument, newCompany);

        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in the wrong format!");
        }
        return true;
    }
}
