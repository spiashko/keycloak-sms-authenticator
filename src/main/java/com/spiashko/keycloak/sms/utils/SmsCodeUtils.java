package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.service.SmsContext;
import org.jboss.resteasy.spi.HttpRequest;

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Random;

@Singleton
public class SmsCodeUtils {

    public boolean validateSmsCode(SmsContext context) {
        String storedCode = context.getAuthenticationSession().getAuthNote("sms_code");
        return validateSmsCode(context.getHttpRequest(), storedCode);
    }

    public boolean smsStoreIsEmpty(SmsContext context) {
        return context.getAuthenticationSession().getAuthNote("sms_code") == null;
    }

    public void storeSmsCode(SmsContext context, String code) {
        context.getAuthenticationSession().setAuthNote("sms_code", code);
    }

    public String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    private boolean validateSmsCode(HttpRequest httpRequest, String storedCode) {
        MultivaluedMap<String, String> formData = httpRequest.getDecodedFormParameters();
        String smsCode = formData.getFirst(Constants.ANSW_SMS_CODE);
        return storedCode.equals(smsCode);
    }

}
