package com.spiashko.keycloak.sms.smsproducer;

public class SmsBodyBuilder {

    public static String build(String code, String localeString) {
        return String.format(resolveLocale(localeString), code);
    }

    private static String resolveLocale(String localeString) {

        String defaultValue = "use the following code to login to Patient Portal: %s";

        if (localeString == null) {
            return defaultValue;
        }

        switch (localeString) {
//            case "some locale":
//                return "some message: %s";
            default:
                return defaultValue;
        }

    }

}
