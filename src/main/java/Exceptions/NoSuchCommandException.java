package Exceptions;

public class NoSuchCommandException extends CommandException {

    public NoSuchCommandException(){
        super("Exception in command");
    }
    public NoSuchCommandException(String message){
        super(message);
    }
}
