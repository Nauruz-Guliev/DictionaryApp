package ru.kpfu.itis.gnt.languagelearningapp.exception;

import org.springframework.http.HttpStatus;

public class InvalidFieldDataException extends LLAPPException {

    public InvalidFieldDataException(String message) {
        super(message);
    }
}
