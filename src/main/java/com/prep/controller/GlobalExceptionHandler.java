package com.prep.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔥 Handle RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntime(RuntimeException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return error;
    }

    // 🔥 Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGeneral(Exception ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Something went wrong");

        return error;
    }
}
