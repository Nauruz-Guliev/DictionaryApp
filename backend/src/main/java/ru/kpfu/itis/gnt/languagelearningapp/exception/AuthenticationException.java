package ru.kpfu.itis.gnt.languagelearningapp.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends LLAPPException {

    private final HttpStatus code;
    public AuthenticationException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
