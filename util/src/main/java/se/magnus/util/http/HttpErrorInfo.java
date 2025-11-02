package se.magnus.util.http;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;


public record HttpErrorInfo(ZonedDateTime timestamp, String path, HttpStatus httpStatus, String message) {

    public HttpErrorInfo(
            final HttpStatus httpStatus,
            final String path,
            final String message
    ) {
        this(ZonedDateTime.now(), path, httpStatus, message);
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getError() {
        return httpStatus.getReasonPhrase();
    }

}
