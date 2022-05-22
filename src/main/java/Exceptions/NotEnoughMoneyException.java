package Exceptions;

public class NotEnoughMoneyException extends CommandException {
    public NotEnoughMoneyException(String message){
        super(message);
    }
}
