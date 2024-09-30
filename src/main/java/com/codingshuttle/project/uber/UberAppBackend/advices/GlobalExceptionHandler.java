package com.codingshuttle.project.uber.UberAppBackend.advices;


import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.RuntimeConflictExceptions;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ResourceNotFoundExceptions.class) //2.8vdo
    public ResponseEntity<ApiResponse<?>> notFoundResource(ResourceNotFoundExceptions rnfe){  //here we use ApiResponse instead of ApiError
        ApiError apiE = ApiError.builder().
                status(HttpStatus.NOT_FOUND).
                message(rnfe.getMessage()).
                build();
        //return new ResponseEntity<>(apiE,HttpStatus.NOT_FOUND); //after changing this is getting an error
        // we have to make a method
        return buildErrorResponseEntity(apiE); //2.8vdo
    }
    @ExceptionHandler(RuntimeConflictExceptions.class) //2.8vdo
    public ResponseEntity<ApiResponse<?>> notFoundRuntimeConflictException(RuntimeConflictExceptions rnfe){  //here we use ApiResponse instead of ApiError
        ApiError apiE = ApiError.builder().
                status(HttpStatus.CONFLICT).
                message(rnfe.getMessage()).
                build();
        //return new ResponseEntity<>(apiE,HttpStatus.NOT_FOUND); //after changing this is getting an error
        // we have to make a method
        return buildErrorResponseEntity(apiE); //2.8vdo
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServiceError(Exception e){//2.8vdo
        ApiError apiE = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
        return buildErrorResponseEntity(apiE);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>>handledInputValidationError(MethodArgumentNotValidException e){ //2.8vdo
        List<String> errors =  e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        ApiError apiE = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Input validation failed")
                .subErrors(errors)
                .build();
        return buildErrorResponseEntity(apiE);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiE) { //2.8vdo
        return new ResponseEntity<>(new ApiResponse<>(apiE),apiE.getStatus()); //getHs() is nothing but getStatus...
    }
    @ExceptionHandler(AuthenticationException.class) //5.8vdo
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException e){
        //ApiError apiError = new ApiError(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getMessage())
                .build();
        //return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED); //return type apierror
        return buildErrorResponseEntity(apiError);
    }

    @ExceptionHandler(JwtException.class) //5.8vdo
    public ResponseEntity<ApiResponse<?>> handleJwtException(JwtException e){
        //ApiError apiError = new ApiError(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message(e.getMessage())
                .build();
        //return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
        return buildErrorResponseEntity(apiError);
    }

    //6.6vdo
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e){
       // ApiError apiError = new ApiError(e.getLocalizedMessage(), HttpStatus.FORBIDDEN);
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.FORBIDDEN)
                .message(e.getMessage())
                .build();
       // return new ResponseEntity<>(apiError,HttpStatus.FORBIDDEN);
        return buildErrorResponseEntity(apiError);
    }

}
