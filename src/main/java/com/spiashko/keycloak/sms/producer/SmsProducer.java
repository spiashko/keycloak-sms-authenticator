package com.spiashko.keycloak.sms.producer;

public interface SmsProducer {

    void produce(String smsMessage, String destinationNumber);

}
