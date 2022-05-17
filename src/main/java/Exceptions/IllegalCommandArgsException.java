package Exceptions;

public class IllegalCommandArgsException extends CommandException {

    public IllegalCommandArgsException(){
        super("Exception in command");
    }
    public IllegalCommandArgsException(String message){
        super(message);
    }
}
