package by.nickshock.canary.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class CanaryController {

    @GetMapping("/get")
    public ResponseEntity<CanaryResponse> getRequest(@RequestParam(defaultValue = "200") int code) {
        return responseFromStatus(HttpStatus.valueOf(code));
    }

    private ResponseEntity<CanaryResponse> responseFromStatus(HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new CanaryResponse(status));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CanaryResponse> handleExceptions() {
        return responseFromStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
