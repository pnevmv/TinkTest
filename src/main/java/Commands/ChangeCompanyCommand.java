package Commands;

import Connection.Connector;
import Data.Company;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.CompanyNotFoundException;
import Exceptions.ExchangeUnavailableException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.math.BigDecimal;

/**
 * Class command for changing parameters
 * that bot can use for trading stocks of this company
 */
public class ChangeCompanyCommand extends AbstractCommand{
    private final CompanyBuilder companyBuilder;
    private final CompanyCollection companyCollection;
    private final Connector connector;

    public ChangeCompanyCommand(CompanyBuilder companyBuilder, CompanyCollection companyCollection, Connector connector) {
        super("change-parameters {figi}", "Change money allowed for company");
        this.companyBuilder = companyBuilder;
        this.companyCollection = companyCollection;
        this.connector = connector;
    }

    /**
     * @param argument - figi of company to change
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in the wrong format!");
            if (connector.isAvailableNow(companyBuilder.askNameOfExchange())) throw new ExchangeUnavailableException("Exchange unavailable now, check the schedule");
            if (connector.isExistByFigi(argument)) throw new CompanyNotFoundException("No shares with this figi were found");

            companyCollection.removeByFigi(argument);

            double moneyToTrade = companyBuilder.askMoneyToTrade();
            if (BigDecimal.valueOf(moneyToTrade).compareTo(connector.getAmountOfMoney()) > 0) throw new IllegalCommandArgsException("Wrong value of money");
            double lossPercent = companyBuilder.askLossPercent();
            double takeProfit = companyBuilder.askTakeProfit();
            int lot = connector.getLotByFigi(argument);

            Company newCompany = new Company(argument, moneyToTrade, lossPercent, takeProfit, lot);
            companyCollection.putCompanyByFigi(argument, newCompany);
            Console.println("Company has been changed successfully");
        } catch (IllegalCommandArgsException | ExchangeUnavailableException | CompanyNotFoundException exception) {
            Console.printError(exception.getMessage());
        }

        return true;
    }
}
