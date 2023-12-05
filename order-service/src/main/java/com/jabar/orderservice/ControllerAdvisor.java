package com.jabar.orderservice;

import com.jabar.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(ThirdServiceException.class)
    OrderService.BaseResponse<Void> thirdAPIException(RuntimeException e) {
        return OrderService.BaseResponse.<Void>builder()
                .statusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(String.format("Cannot find product, %s", e.getMessage()))
                .build();
    }

    public static class ThirdServiceException extends RuntimeException {
        public ThirdServiceException(String message) {
            super(message);
        }

        public ThirdServiceException(Throwable cause) {
            super(cause);
        }
    }
}
