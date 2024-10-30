package com.greeting.filter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends Exception {

    public RateLimitException(final String message) {
        super(message);
    }
}
