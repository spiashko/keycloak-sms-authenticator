package com.spiashko.keycloak.sms.producer;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

@Singleton
@Slf4j
@ToString(callSuper = true)
public class LogsSmsProducer implements SmsProducer {

    @Override
    public void produce(String smsMessage, String destinationNumber) {
        log.info("produce sms with message=" + smsMessage +
                " for destination=" + destinationNumber);
    }
}
