package Exceptions;

public class CompanyNotFoundException extends CommandException {
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
