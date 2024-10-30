package com.greeting.util;

import java.io.IOException;

public class PingFileLockFactory {

    private static final PingFileLockFactory INSTANCE = new PingFileLockFactory();

    // Private constructor to prevent instantiation from outside the class
    private PingFileLockFactory() {
        // Initialization logic can go here
    }

    // Public static method to get the singleton instance
    public static PingFileLockFactory getInstance() {
        return INSTANCE;
    }

    // Add your methods and logic here
    public static PingFileLock produce(String lockFileName,String valueFileName) throws AcquireFileLockException, IOException {
        return new PingFileLock(lockFileName,valueFileName);
    }
}
