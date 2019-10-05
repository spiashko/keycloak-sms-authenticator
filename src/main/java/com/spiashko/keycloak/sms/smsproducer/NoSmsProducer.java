package com.spiashko.keycloak.sms.smsproducer;

import org.jboss.logging.Logger;

public class NoSmsProducer implements SmsProducer {

    private static Logger logger = Logger.getLogger(NoSmsProducer.class);

    @Override
    public void produce(String smsCode, String destinationNumber, String localeString) {
        logger.info("sending to " + destinationNumber + " next sms: " + SmsBodyBuilder.build(smsCode, localeString));
    }
}
