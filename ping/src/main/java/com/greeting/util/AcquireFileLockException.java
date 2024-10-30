package com.greeting.util;

import java.io.Serial;

public class AcquireFileLockException extends Exception{

    @Serial
    private static final long serialVersionUID = 6294063382857239686L;

    public AcquireFileLockException(final String message) {
        super(message);
    }
}
