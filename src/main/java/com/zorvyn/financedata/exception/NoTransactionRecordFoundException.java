package com.zorvyn.financedata.exception;

public class NoTransactionRecordFoundException extends RuntimeException {
    public NoTransactionRecordFoundException(String message) {
        super(message);
    }
}
