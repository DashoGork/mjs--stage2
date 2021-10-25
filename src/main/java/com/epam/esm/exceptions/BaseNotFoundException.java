package com.epam.esm.exceptions;

public class BaseNotFoundException extends RuntimeException{
    public BaseNotFoundException(String message) {
        super(message);
    }
}
