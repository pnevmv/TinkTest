package Commands;


/**
 * User can change amount of money that bot can use for trading stocks of this company
 */
public class ChangeMoneyAllowedForCompanyCommand extends AbstractCommand{

    public ChangeMoneyAllowedForCompanyCommand() {
        super("change-money {figi} {value}", "Change money allowed for company");
    }

    @Override
    public boolean execute(String argument) {
        return true;
    }
}
