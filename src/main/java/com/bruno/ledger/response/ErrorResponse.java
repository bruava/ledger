package com.bruno.ledger.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(int status, String code, String message, String path, Instant timestamp, List<String> details) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, path, Instant.now(), null);
    }

    public static ErrorResponse of(int status, String error, String message, String path, List<String> details) {
        return new ErrorResponse(status, error, message, path, Instant.now(), details);
    }
}
