package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import org.keycloak.models.UserModel;

import javax.inject.Singleton;

@Singleton
public class UserAttributeUtils {

    public String getMobileNumber(UserModel user) {
        String mobileNumberCreds = user.getFirstAttribute(Constants.ATTR_MOBILE);

        String mobileNumber = null;

        if (mobileNumberCreds != null && !mobileNumberCreds.isEmpty()) {
            mobileNumber = mobileNumberCreds;
        }

        return mobileNumber;
    }

}
