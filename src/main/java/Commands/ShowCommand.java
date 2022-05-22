package Commands;

import Data.CompanyCollection;

public class ShowCommand extends AbstractCommand {
    CompanyCollection companyCollection;

    public ShowCommand(CompanyCollection companyCollection) {
        super("show", "Display all companies");
        this.companyCollection = companyCollection;
    }

    @Override
    public boolean execute(String argument) {
        if (!argument.isEmpty()) throw new IllegalArgumentException();

        companyCollection.printCompanies();

        return true;
    }
}
