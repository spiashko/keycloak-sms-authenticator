package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import org.keycloak.models.UserModel;

public class UserAttributeUtils {
    public static String getMobileNumber(UserModel user) {
        String mobileNumberCreds = user.getFirstAttribute(Constants.ATTR_MOBILE);

        String mobileNumber = null;

        if (mobileNumberCreds != null && !mobileNumberCreds.isEmpty()) {
            mobileNumber = mobileNumberCreds;
        }

        return mobileNumber;
    }

    public static Boolean getMobileNumberVerified(UserModel user) {
        String mobileNumberVerified = user.getFirstAttribute(Constants.ATTR_MOBILE_VERIFIED);
        return Boolean.parseBoolean(mobileNumberVerified);
    }

    public static void setMobileNumber(UserModel user, String mobileNumber){
        user.setSingleAttribute(Constants.ATTR_MOBILE, mobileNumber);
    }

    public static void setMobileNumberVerified(UserModel user){
        user.setSingleAttribute(Constants.ATTR_MOBILE_VERIFIED, Boolean.TRUE.toString());
    }
}
