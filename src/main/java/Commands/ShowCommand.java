package Commands;

import Data.Company;
import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.util.Collection;

/**
 * Class command for showing
 * all companies in collection to trade
 */
public class ShowCommand extends AbstractCommand {
    private final CompanyCollection companyCollection;

    public ShowCommand(CompanyCollection companyCollection) {
        super("show", "Display all companies");
        this.companyCollection = companyCollection;
    }

    /**
     * Executes the command
     * @return success/fail execution
     */
    @Override
    public boolean execute(String argument) {
        try {
            if (!argument.isEmpty()) throw new IllegalCommandArgsException();
            Collection<Company> companies = companyCollection.getCompanies().values();
            for (Company company: companies) {
                Console.println(company.toString());
            }
        } catch (IllegalCommandArgsException exception) {
            Console.printError("The command was entered in wrong format!");
        }
        return true;
    }
}
