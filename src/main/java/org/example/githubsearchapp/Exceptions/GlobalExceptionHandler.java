package org.example.githubsearchapp.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ResponseEntity<ErrorResponseModel> handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {

        var response = new ErrorResponseModel(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    @ExceptionHandler(InvalidUserNameException.class)
    ResponseEntity<ErrorResponseModel> handleInvalidUserNameException(InvalidUserNameException ex) {

        var response = new ErrorResponseModel(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleUserNotFoundException(UserNotFoundException ex) {

        var response = new ErrorResponseModel(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

}
