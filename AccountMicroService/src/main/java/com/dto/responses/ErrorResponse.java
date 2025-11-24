package com.dto.responses;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(String message, String error, String path) {
        this.message = message;
        this.error = error;
        this.path = path;
    }

    public String getMessage() { return message; }
    public String getError() { return error; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

