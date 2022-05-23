package Exceptions;

/**
 * Throws when connection to exchange is anavailable
 */
public class ExchangeUnavailableException extends CommandException {
    public ExchangeUnavailableException(String message) {
        super(message);
    }
}
