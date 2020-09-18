package com.spiashko.keycloak.sms.service;

import com.google.inject.Inject;
import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.producer.LogsSmsProducer;
import com.spiashko.keycloak.sms.producer.SmsProducer;
import com.spiashko.keycloak.sms.utils.MobileNumberUtils;
import com.spiashko.keycloak.sms.utils.SmsCodeUtils;
import com.spiashko.keycloak.sms.utils.UserAttributeUtils;
import org.jboss.logging.Logger;
import org.keycloak.models.UserModel;
import org.keycloak.theme.Theme;

import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Locale;

@Singleton
public class SmsService {

    public static final String SMS_FORM_STATE = "sms_verification_form_state";
    public static final String SMS_CODE_INPUT = "sms_code_input";
    public static final String MOBILE_NUMBER_INPUT = "mobile_number_input";
    public static final String CUSTOM_STYLES_ATTRIBUTE = "customStyles";
    public static final String INTL_TEL_INPUT_MIN_CSS = "intlTelInput.min.css";

    private static final Logger logger = Logger.getLogger(SmsService.class);

    private final SmsProducer smsProducer;
    private final SmsCodeUtils smsCodeUtils;
    private final MobileNumberUtils mobileNumberUtils;
    private final UserAttributeUtils userAttributeUtils;

    @Inject
    public SmsService(
            LogsSmsProducer logsSmsProducer,
            SmsCodeUtils smsCodeUtils,
            MobileNumberUtils mobileNumberUtils,
            UserAttributeUtils userAttributeUtils
    ) {
        this.smsProducer = logsSmsProducer;
        this.smsCodeUtils = smsCodeUtils;
        this.mobileNumberUtils = mobileNumberUtils;
        this.userAttributeUtils = userAttributeUtils;
    }

    public void smsChallenge(SmsContext context) {

        if (isResendSmsReload(context)) {
            showResendForm(context);
            return;
        }

        UserModel user = context.getUser();
        String mobileNumber = userAttributeUtils.getMobileNumber(user);
        Locale locale = context.getSession().getContext().resolveLocale(context.getUser());

        String smsMessageFormat;
        try {
            smsMessageFormat = context.getSession().theme()
                    .getTheme(Theme.Type.LOGIN)
                    .getMessages(locale)
                    .getProperty(Constants.MESSAGE_SMS_TEXT);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (smsMessageFormat == null) {
            throw new SmsServiceException("failed to find sms message format");
        }

        if (smsCodeUtils.smsStoreIsEmpty(context)) {
            String code = smsCodeUtils.generateCode();
            String message = getSmsMessageBody(locale, smsMessageFormat, code);
            try {
                smsProducer.produce(message, mobileNumber);
            } catch (Exception ex) {
                throw new SmsServiceException("failed to produce sms message for username=" + user.getUsername(), ex);
            }
            smsCodeUtils.storeSmsCode(context, code);
        }

        Response challenge = context.form()
                .setInfo(Constants.MESSAGE_SMS_CODE_PROMPT)
                .createForm(Constants.FORM_SMS_VALIDATION);
        context.challenge(challenge);

        context.getAuthenticationSession().setAuthNote(SMS_FORM_STATE, SMS_CODE_INPUT);
    }

    public void verifySmsChallenge(SmsContext context) {
        if (SMS_CODE_INPUT.equals(context.getAuthenticationSession().getAuthNote(SMS_FORM_STATE))) {
            MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
            if (formData.containsKey(Constants.ANSW_RESEND_SMS_CODE)) {
                showResendForm(context);
                return;
            }

            boolean validated = smsCodeUtils.validateSmsCode(context);
            if (!validated) {
                Response challenge = context.form()
                        .setError(Constants.MESSAGE_SMS_CODE_NO_VALID)
                        .createForm(Constants.FORM_SMS_VALIDATION);
                context.challenge(challenge);
                return;
            }

            context.success();
        } else {
            MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
            if (formData.containsKey(Constants.ANSW_CANCEL_RESEND_SMS_CODE)) {
                Response challenge = context.form()
                        .setInfo(Constants.MESSAGE_SMS_CODE_PROMPT)
                        .createForm(Constants.FORM_SMS_VALIDATION);
                context.challenge(challenge);
                context.getAuthenticationSession().setAuthNote(SMS_FORM_STATE, SMS_CODE_INPUT);
                return;
            }

            String providedMobileNumber = formData.getFirst(Constants.ANSW_MOBILE_NUMBER);
            String existingMobileNumber = userAttributeUtils.getMobileNumber(context.getUser());

            String errorMessageKey = mobileNumberUtils.validateMobileNumber(existingMobileNumber, providedMobileNumber);
            if (errorMessageKey == null) {
                logger.info(String.format("resend sms request for %s was successful", existingMobileNumber));
                smsCodeUtils.storeSmsCode(context, null);
                context.getAuthenticationSession().setAuthNote(SMS_FORM_STATE, SMS_CODE_INPUT);
                smsChallenge(context);
            } else {
                logger.info(String.format("resend sms request for %s was failed due to %s", existingMobileNumber, errorMessageKey));
                Response challenge = context.form()
                        .setError(errorMessageKey)
                        .setAttribute(CUSTOM_STYLES_ATTRIBUTE, INTL_TEL_INPUT_MIN_CSS)
                        .createForm(Constants.FROM_RESEND_SMS);
                context.challenge(challenge);
            }
        }
    }

    private void showResendForm(SmsContext context) {
        Response challenge = context.form()
                .setInfo(Constants.MESSAGE_MOBILE_NUMBER_PROMPT)
                .setAttribute(CUSTOM_STYLES_ATTRIBUTE, INTL_TEL_INPUT_MIN_CSS)
                .createForm(Constants.FROM_RESEND_SMS);
        context.challenge(challenge);

        context.getAuthenticationSession().setAuthNote(SMS_FORM_STATE, MOBILE_NUMBER_INPUT);
    }

    private boolean isResendSmsReload(SmsContext context) {
        return MOBILE_NUMBER_INPUT.equals(context.getAuthenticationSession().getAuthNote(SMS_FORM_STATE));
    }

    private String getSmsMessageBody(Locale locale, String smsMessageFormat, String code) {
        Object[] args = new Object[1];
        args[0] = code;
        return new MessageFormat(smsMessageFormat, locale).format(args, new StringBuffer(), new FieldPosition(0)).toString();
    }
}
