package com.codingshuttle.project.uber.UberAppBackend.exceptions;

public class ResourceNotFoundExceptions extends RuntimeException{
    public ResourceNotFoundExceptions() {
    }

    public ResourceNotFoundExceptions(String message) {
        super(message);
    }
}
