package com.secureauthsystem.exception;

/**
 * Custom exception thrown when a user already exists in the system.
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message); // Passes the message to the RuntimeException constructor
    }
}
