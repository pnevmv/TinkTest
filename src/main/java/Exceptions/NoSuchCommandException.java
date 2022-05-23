package Exceptions;

/**
 * Throws when inputted command isnt exist
 */
public class NoSuchCommandException extends CommandException {

    public NoSuchCommandException(){
        super("Exception in command");
    }
    public NoSuchCommandException(String message){
        super(message);
    }
}
