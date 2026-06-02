package com.jobportal.backend.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex
    ){

        return ResponseEntity.status(
                HttpStatus.NOT_FOUND
        ).body(
                new ErrorResponse(
                        ex.getMessage(),
                        404
                )
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex
    ){
        return ResponseEntity.status(
                HttpStatus.FORBIDDEN
        ).body(
                new ErrorResponse(
                    ex.getMessage(),
                    403
                )
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex
    ){
        return ResponseEntity.status(
                HttpStatus.BAD_REQUEST
        ).body(
                new ErrorResponse(
                        ex.getMessage(),
                        400
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex
    ){
        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
        ).body(
                new ErrorResponse(
                        ex.getMessage(),
                        500
                )
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex
    ){
        String message =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(
                        new ErrorResponse(
                                message,
                                400
                        )
                );
    }

}
