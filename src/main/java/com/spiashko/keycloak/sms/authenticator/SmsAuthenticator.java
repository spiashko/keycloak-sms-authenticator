package com.spiashko.keycloak.sms.authenticator;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.requiredaction.MobileNumberRequiredActionFactory;
import com.spiashko.keycloak.sms.smsproducer.SmsProducerFactory;
import com.spiashko.keycloak.sms.utils.CookieUtils;
import com.spiashko.keycloak.sms.utils.SmsCodeUtils;
import com.spiashko.keycloak.sms.utils.UserAttributeUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class SmsAuthenticator implements Authenticator {

    private static Logger logger = Logger.getLogger(SmsAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        logger.debug("authenticate called ...");

        if (CookieUtils.hasCookie(context)) {
            logger.info("Bypassing smsCode because cookie is set");
            context.success();
            return;
        }

        UserModel user = context.getUser();
        String mobileNumber = UserAttributeUtils.getMobileNumber(user);
        String localeString = user.getFirstAttribute(UserModel.LOCALE);

        if (mobileNumber == null || !UserAttributeUtils.getMobileNumberVerified(user)) {
            context.getAuthenticationSession().addRequiredAction(MobileNumberRequiredActionFactory.PROVIDER_ID);
            context.success();
            return;
        }

        String code = SmsCodeUtils.generateCode();
        logger.info("code: " + code + " for: " + mobileNumber);

        SmsProducerFactory.getInstance().produce(code, mobileNumber, localeString);

        SmsCodeUtils.storeSmsCode(context, code);

        Response challenge = context.form().createForm(Constants.FORM_SMS_VALIDATION);
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        logger.debug("action called ...");

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey(Constants.ANSW_CHANGE_MOBILE_NUMBER)) {
            context.getAuthenticationSession().addRequiredAction(MobileNumberRequiredActionFactory.PROVIDER_ID);
            context.success();
            return;
        }

        boolean validated = SmsCodeUtils.validateSmsCode(context);
        if (!validated) {
            Response challenge = context.form()
                    .setError(Constants.MESSAGE_SMS_CODE_NO_VALID)
                    .createForm(Constants.FORM_SMS_VALIDATION);
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }

        CookieUtils.setCookie(context);

        context.success();
    }

    @Override
    public boolean requiresUser() {
        logger.debug("requiresUser called ...");
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        logger.debug("configuredFor called ...");
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        logger.debug("setRequiredActions called ...");
    }

    @Override
    public void close() {
        logger.debug("close called ...");
    }
}
