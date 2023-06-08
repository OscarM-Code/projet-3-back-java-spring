package com.projetjavaopc.api.error;

public class CreateMessageException extends RuntimeException {
    public CreateMessageException(String message) {
        super(message);
    }
}
