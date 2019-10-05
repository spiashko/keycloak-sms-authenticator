package com.spiashko.keycloak.sms.requiredaction;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.smsproducer.SmsProducerFactory;
import com.spiashko.keycloak.sms.utils.CookieUtils;
import com.spiashko.keycloak.sms.utils.SmsCodeUtils;
import com.spiashko.keycloak.sms.utils.UserAttributeUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class VerifyMobileNumberRequiredAction implements RequiredActionProvider {

    private static Logger logger = Logger.getLogger(VerifyMobileNumberRequiredAction.class);

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        logger.debug("evaluateTriggers called ...");
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        logger.debug("requiredActionChallenge called ...");

        UserModel user = context.getUser();
        String mobileNumber = UserAttributeUtils.getMobileNumber(user);
        String localeString = user.getFirstAttribute(UserModel.LOCALE);

        if (mobileNumber == null) {
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
    public void processAction(RequiredActionContext context) {
        logger.debug("processAction called ...");

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
            context.challenge(challenge);
            return;
        }

        UserAttributeUtils.setMobileNumberVerified(context.getUser());

        CookieUtils.setCookie(context);

        context.success();
    }

    @Override
    public void close() {
        logger.debug("close called ...");
    }
}
