package Exceptions;

/**
 * Throws when there are not trading account on this token
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) { super(message); }
}
