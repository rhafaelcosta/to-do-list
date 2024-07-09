package com.github.rhafaelcosta.todolist.exceptions;

public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
