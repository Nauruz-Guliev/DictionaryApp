package ru.kpfu.itis.gnt.languagelearningapp.advice;

import com.xkcoding.http.support.Http;
import kotlin.io.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kpfu.itis.gnt.languagelearningapp.constants.ErrorMessageConstants;
import ru.kpfu.itis.gnt.languagelearningapp.exception.AuthenticationException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.InvalidFieldDataException;
import ru.kpfu.itis.gnt.languagelearningapp.exception.ResourceNotFoundException;
import ru.kpfu.itis.gnt.languagelearningapp.model.ErrorModel;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorModel> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(ex.getCode()).body(ErrorModel.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {InvalidFieldDataException.class})
    public ResponseEntity<ErrorModel> handleDataException(InvalidFieldDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorModel.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorModel> handleEmptyData(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorModel.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorModel> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorModel.builder().message(ErrorMessageConstants.NOT_AUTHORIZED.ERROR_MESSAGE).build());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<ErrorModel> handleNullPointer(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorModel.builder().message(ex.getMessage()).build());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorModel> handleAnyException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorModel.builder().message(ErrorMessageConstants.INTERNAL.UNKNOWN + ": " + ex.getMessage()).build());
    }
}
