package com.spiashko.keycloak.sms.smsproducer;

public interface SmsProducer {

    void produce(String smsCode, String destinationNumber, String localeString);

}
