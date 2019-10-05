package com.spiashko.keycloak.sms.smsproducer;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.smsproducer.twilio.TwilioSmsProducer;

public class SmsProducerFactory {

    private static final SmsProducer SINGLETON =
            Boolean.parseBoolean(System.getenv(Constants.ENV_VAR_SEND_REAL_SMS)) ?
                    new TwilioSmsProducer() : new NoSmsProducer();

    public static SmsProducer getInstance() {
        return SINGLETON;
    }
}
