package by.nickshock.canary.rest;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class CanaryResponse {

    private int code;
    private HttpStatus status;
    private OffsetDateTime timestamp;

    public CanaryResponse(HttpStatus status) {
        code = status.value();
        this.status = status;
        timestamp = OffsetDateTime.now();
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
