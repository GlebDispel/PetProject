package ru.glebdos.clientmicroservice.util;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleGenericException(Exception e) {
        LOGGER.error("Unexpected error: {}", e.getMessage(), e);
        ClientErrorResponse response = new ClientErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //ловит исключения, связанные с нарушением валидации полей
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ClientErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage())
                    .append(";");
        }
        ClientErrorResponse response = new ClientErrorResponse(
                errorMessage.toString(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    //Ловит ошибки, возникающие при неверном формате тела запроса, например, синтаксические ошибки JSON.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ClientErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Ловит ошибки, связанные с отсутствием объекта в базе данных
    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ClientErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ClientErrorResponse response = new ClientErrorResponse(
                e.getLocalizedMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}


