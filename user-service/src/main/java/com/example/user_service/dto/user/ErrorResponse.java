package com.example.user_service.dto.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private Instant timestamp;
    private String path;
    private List<String> details;

    public ErrorResponse(int status, String error, String message, Instant timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ErrorResponse(int status, String error, String message,
                         Instant timestamp, List<String> details) {
        this(status, error, message, timestamp);
        this.details = details;
    }
}
