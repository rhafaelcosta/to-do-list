package com.github.rhafaelcosta.todolist.exceptions;

public class EnumNotFoundException extends Exception {

    public EnumNotFoundException(String message) {
        super(message);
    }

    public EnumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
