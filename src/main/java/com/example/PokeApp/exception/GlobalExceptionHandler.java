package com.example.PokeApp.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

//TODO find out why swagger ui throws 500
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiError> handleExternalApiException(RestClientException ex) {
        HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "The external Pokemon API is currently unavailable.";

        if (ex instanceof HttpStatusCodeException httpException) {
            status = httpException.getStatusCode();
            message = "External API returned: " + httpException.getStatusText();
        }

        ApiError error = new ApiError(
                message,
                status.value(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(PokemonNotFoundException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ApiError error = new ApiError(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}