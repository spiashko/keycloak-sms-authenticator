package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import org.apache.commons.lang.StringUtils;

import javax.inject.Singleton;

@Singleton
public class MobileNumberUtils {

    /**
     * @return return error message code if error of null if fine
     */
    public String validateMobileNumber(
            String existingMobileNumber,
            String providedMobileNumber) {
        if (StringUtils.isEmpty(providedMobileNumber)) {
            return Constants.MESSAGE_MOBILE_NUMBER_NO_VALID;
        }

        String provided = providedMobileNumber.trim();
        String existing = existingMobileNumber.trim();

        if (!provided.equals(existing)) {
            return Constants.MESSAGE_MOBILE_NUMBER_NOT_EQUAL;
        }
        return null;
    }

}
