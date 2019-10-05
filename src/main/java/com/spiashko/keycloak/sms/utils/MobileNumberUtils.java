package com.spiashko.keycloak.sms.utils;

public class MobileNumberUtils {

    public static boolean validate(String mobileNumber){
        return mobileNumber != null &&
                mobileNumber.length() > 0 &&
                mobileNumber.matches("^\\+(?:[0-9] ?){6,14}[0-9]$");
    }

}
