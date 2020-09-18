package com.spiashko.keycloak.sms.service;

public class SmsServiceException extends RuntimeException {

    public SmsServiceException(String message) {
        super(message);
    }

    public SmsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
