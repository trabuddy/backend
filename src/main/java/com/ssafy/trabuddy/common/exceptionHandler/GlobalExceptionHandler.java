package com.ssafy.trabuddy.common.exceptionHandler;

import com.ssafy.trabuddy.common.error.ErrorCode;
import com.ssafy.trabuddy.common.error.ErrorResult;
import com.ssafy.trabuddy.common.error.TrabuddyException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handler(ConstraintViolationException ex) {
        Map<String, String> map = new HashMap<>();
        map.put("에러 메세지", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler2(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();
//        ex.getBindingResult().getFieldErrors()
//                .forEach(error -> map.put(error.getField(), error.getDefaultMessage()));
        map.put("message", ex.getBindingResult().getFieldError().getDefaultMessage());

        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(TrabuddyException.class)
    public ResponseEntity<ErrorResult> handler(TrabuddyException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ErrorResult(errorCode.getMessage()));
    }
}
