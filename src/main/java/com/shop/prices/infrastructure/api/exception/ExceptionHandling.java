package com.shop.prices.infrastructure.api.exception;

import com.shop.prices.domain.exception.DomainEntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorResponse> parseException(HttpServletRequest req, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "error parsing parameters"
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DomainEntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> domainEntityNotFoundException(HttpServletRequest req, Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
