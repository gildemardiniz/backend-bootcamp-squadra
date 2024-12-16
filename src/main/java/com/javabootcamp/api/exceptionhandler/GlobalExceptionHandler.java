package com.javabootcamp.api.exceptionhandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.javabootcamp.api.error.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ErrorDto> handle(JsonParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Insira um valor válido no campo "+ e.getProcessor().getParsingContext().getCurrentName(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorDto> handle(InvalidFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Formato inválido para o campo "+ e.getPath().getFirst().getFieldName(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handle(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Tipo de parâmetro inválido para busca por "+ e.getName() , HttpStatus.BAD_REQUEST.value()));
    }
}
