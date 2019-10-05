package com.spiashko.keycloak.sms;

public class Constants {

    public static final String COOKIE_SMS_CODE_PROVIDED = "SMS_CODE_PROVIDED";

    // User attributes
    public static final String ATTR_MOBILE = "mobile_number";
    public static final String ATTR_MOBILE_VERIFIED = "mobile_number_verified";

    // Answer field names
    public static final String ANSW_SMS_CODE = "sms_code";
    public static final String ANSW_MOBILE_NUMBER = "mobile_number";
    public static final String ANSW_CHANGE_MOBILE_NUMBER = "change_number";
    public static final String ANSW_CANCEL_CHANGE_MOBILE_NUMBER = "cancel_change_number";

    // Froms
    public static final String FORM_MOBILE_NUMBER = "mobile-number.ftl";
    public static final String FORM_SMS_VALIDATION = "sms-validation.ftl";

    // Messages
    public static final String MESSAGE_MOBILE_NUMBER_NO_VALID = "mobile_number.no.valid";
    public static final String MESSAGE_SMS_CODE_NO_VALID = "sms_code.no.valid";

    // env vars
    public static final String ENV_VAR_SEND_REAL_SMS = "SEND_REAL_SMS";

    private Constants(){}
}
