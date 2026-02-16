package com.githubsearcher.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
