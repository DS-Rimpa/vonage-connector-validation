package com.company.model.response;

import com.company.util.Constants;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String exception;
    private String time;
    private String status;
    private int statusCode;
    private ErrorResponse(
            String message, String exception, String time, String status, int statusCode) {
        this.message = message;
        this.exception = exception;
        this.time = time;
        this.status = status;
        this.statusCode = statusCode;
    }
    public static ErrorResponse create(Exception ex) {
        return new ErrorResponse(
                ex.getMessage(),
                ex.getClass().getName(),
                Instant.now().toString(),
                Constants.Status.FAILED,
                HttpStatus.BAD_REQUEST.value());
    }
}
