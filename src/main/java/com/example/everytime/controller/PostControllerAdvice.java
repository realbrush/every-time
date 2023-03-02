package com.example.everytime.controller;

import com.example.everytime.service.PostServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostControllerAdvice {
    //service error catch
    @ExceptionHandler(PostServiceImpl.PostCreationException.class)
    public ResponseEntity<String> handlePostCreationException(PostServiceImpl.PostCreationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PostServiceImpl.PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostServiceImpl.PostNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
