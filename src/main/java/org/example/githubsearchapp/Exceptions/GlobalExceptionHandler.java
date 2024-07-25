package org.example.githubsearchapp.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ResponseEntity<ErrorResponseModel> handleMediaTypeNotAcceptableException() {
        var response = new ErrorResponseModel(HttpStatus.NOT_ACCEPTABLE.value(), "Use media type: " + MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    @ExceptionHandler(InvalidUserNameException.class)
    ResponseEntity<ErrorResponseModel> handleInvalidUserNameException() {
        var response = new ErrorResponseModel(HttpStatus.BAD_REQUEST.value(), "Invalid user name");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleUserNotFoundException() {
        var response = new ErrorResponseModel(HttpStatus.NOT_FOUND.value(), "User not found");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.status()));
    }

}
