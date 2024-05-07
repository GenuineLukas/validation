package com.example.validation.exception;

import com.example.validation.model.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api> validateException(
            MethodArgumentNotValidException e
    ){
        log.error(e.getMessage());

        var errorMessageList = e.getFieldErrors().stream()
                .map(it -> {
                    var format = "%s : { %s } %s";
                    var message = String.format(format, it.getField(), it.getRejectedValue() , it.getDefaultMessage());
                    return message;
                }).collect(Collectors.toList());

        var error = Api.Error
                .builder()
                .errorMessage(errorMessageList)
                .build();

        var errorResponse = Api
                .builder()
                .resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .resultMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .erorr(error)
                .build()
                ;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
