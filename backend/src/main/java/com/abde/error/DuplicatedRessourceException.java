package com.abde.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicatedRessourceException extends RuntimeException {
    public DuplicatedRessourceException(String message) {
        super(message);
    }
}
