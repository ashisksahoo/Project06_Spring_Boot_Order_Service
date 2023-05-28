package com.techpalle.exception;

import com.techpalle.payload.response.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(OrderServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(OrderServiceCustomException exception)
    {
        new ErrorResponse();
        return new ResponseEntity<>(ErrorResponse.builder().
                errorMessage(exception.getMessage()).
                errorCode(exception.getErrorCode()).build(),
                HttpStatusCode.valueOf(exception.getStatus()));
    }
}
