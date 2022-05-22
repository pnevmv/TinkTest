package Commands;

import Data.Company;
import Data.CompanyCollection;
import Exceptions.IllegalCommandArgsException;
import UI.Console.Console;

import java.util.Collection;

public class ShowCommand extends AbstractCommand {
    private CompanyCollection companyCollection;

    public ShowCommand(CompanyCollection companyCollection) {
        super("show", "Display all companies");
        this.companyCollection = companyCollection;
    }

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
