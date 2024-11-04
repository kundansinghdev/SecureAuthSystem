package com.secureauthsystem.exception;

/**
 * Custom exception thrown when a user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message); // Passes the message to the RuntimeException constructor
    }
}
