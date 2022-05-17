package Commands;

import Data.Company;
import Data.CompanyCollection;
import Exceptions.CommandException;
import Exceptions.IllegalCommandArgsException;
import Exceptions.NoSuchCommandException;

public class AddCompany implements Command{
    CompanyCollection companies;

    public AddCompany(CompanyCollection companies){
        this.companies = companies;
    }
    /**
     *
     * @param argsSource - figi, moneyToTrade, lossPercent, TakeProfit
     */
    @Override
    public void execute(CommandArgsSource argsSource) throws CommandException {
        var args = argsSource.getArgsByCommand(Commands.Commands.CommandType.ADD).get();

        if(args.size() != 4) throw new IllegalCommandArgsException("Illegal Number of args");
        String figi = args.get(0);
        //проверка валидности акции
        //получить лотность

        int lot = 0;
        double money = Double.parseDouble(args.get(1));
        double loss = Double.parseDouble(args.get(2));
        double takeProfit = Double.parseDouble(args.get(3));

        companies.putCompanyByFigi(figi, new Company(figi, money, loss, takeProfit, lot));



    }
}
