package ru.kpfu.itis.gnt.languagelearningapp.advice;

import kotlin.io.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.exception.AuthenticationException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.DatabaseException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.InvalidFieldDataException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.ResourceNotFoundException;
import ru.kpfu.itis.gnt.languagelearningapp.dto.ErrorDto;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorDto> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(ex.getCode() == null ? HttpStatus.FORBIDDEN : ex.getCode()).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {InvalidFieldDataException.class})
    public ResponseEntity<ErrorDto> handleDataException(InvalidFieldDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorDto> handleEmptyData(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorDto> handleNullPointer(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {DatabaseException.class})
    public ResponseEntity<ErrorDto> handleDatabaseException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorDto> handleAnyException(Exception ex) {
        System.err.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.builder().message(ErrorMessageConstants.INTERNAL.UNKNOWN).build());
    }
}
