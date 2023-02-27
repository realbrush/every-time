package com.example.everytime.controller;

import com.example.everytime.service.PostServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostControllerAdvice {
    @ExceptionHandler(PostServiceImpl.PostCreationException.class)
    public ResponseEntity<String> handlePostCreationException(PostServiceImpl.PostCreationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
