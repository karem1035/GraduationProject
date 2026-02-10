package com.example.Toda.exception;

public class TouristNotFoundException extends RuntimeException {
    public TouristNotFoundException(String message) {
        super(message);
    }
}
