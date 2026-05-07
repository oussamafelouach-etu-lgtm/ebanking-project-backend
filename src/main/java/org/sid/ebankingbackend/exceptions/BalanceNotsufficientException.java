package org.sid.ebankingbackend.exceptions;

public class BalanceNotsufficientException extends Throwable {
    public BalanceNotsufficientException(String message) {
        super(message);
    }
}
