package com.spiashko.keycloak.sms.smsproducer.twilio;

import com.spiashko.keycloak.sms.smsproducer.SmsBodyBuilder;
import com.spiashko.keycloak.sms.smsproducer.SmsProducer;
import org.jboss.logging.Logger;

public class TwilioSmsProducer implements SmsProducer {

    private static Logger logger = Logger.getLogger(TwilioSmsProducer.class);

    @Override
    public void produce(String smsCode, String destinationNumber, String localeString) {
        logger.info("sending to " + destinationNumber + " next sms: " + SmsBodyBuilder.build(smsCode, localeString));
        //TODO: implement some day
    }
}
