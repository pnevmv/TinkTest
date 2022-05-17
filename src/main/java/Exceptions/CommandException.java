package Exceptions;

public class CommandException extends Exception{
    public CommandException(){
        super("Exception in command");
    }
    public CommandException(String message){
        super(message);
    }
}
