package Commands;

import Connection.Connector;
import Data.Company;
import Data.CompanyBuilder;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.CompanyNotFoundException;
import Exceptions.ExchangeUnavailableException;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.math.BigDecimal;


/**
 * Class command for adding new company in collection
 */
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
     * @param argument - figi of company to add
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) throws CommandException {
        try {
            if (argument.isEmpty()) throw new IllegalCommandArgsException("The command was entered in the wrong format!");
            if (connector.isAvailableNow(companyBuilder.askNameOfExchange())) throw new ExchangeUnavailableException("Exchange unavailable now, check the schedule");
            if (connector.isExistByFigi(argument)) throw new CompanyNotFoundException("No shares with this figi were found");

            double lossPercent = companyBuilder.askLossPercent();
            double takeProfit = companyBuilder.askTakeProfit();
            double moneyToTrade = companyBuilder.askMoneyToTrade();
            int lot = connector.getLotByFigi(argument);

            if (BigDecimal.valueOf(moneyToTrade).compareTo(connector.getAmountOfMoney()) > 0) throw new IllegalCommandArgsException("Wrong value of money");

            Company company = new Company(argument, moneyToTrade, lossPercent, takeProfit, lot);
            companyCollection.putCompanyByFigi(argument, company);
            Console.println("Company successfully added!");
        } catch (IllegalCommandArgsException | ExchangeUnavailableException exception) {
            Console.printError(exception.getMessage());
        }

        return true;
    }
}
