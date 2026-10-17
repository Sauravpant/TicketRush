package com.ticketrush.authservice.exception;

import java.time.Instant;

public class AccountLockedException extends RuntimeException {

    public AccountLockedException(Instant date) {
        super("Account locked until " + date);
    }
}
