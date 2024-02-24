package com.restropos.systemcore.exception;

import com.restropos.systemcore.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class,VerificationRequiredException.class, AlreadyUsedException.class, TimeExceededException.class,WrongCredentialsException.class})
    public ResponseEntity<ResponseMessage> handleDistinctException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }
}