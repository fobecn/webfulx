package com.greeting.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends Exception {

    @Serial
    private static final long serialVersionUID = 9100810528591970074L;

    public RateLimitException(final String message) {
        super(message);
    }
}
