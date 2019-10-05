package com.spiashko.keycloak.sms.requiredaction;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.utils.MobileNumberUtils;
import com.spiashko.keycloak.sms.utils.UserAttributeUtils;
import org.jboss.logging.Logger;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class MobileNumberRequiredAction implements RequiredActionProvider {

    private static Logger logger = Logger.getLogger(MobileNumberRequiredAction.class);

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        logger.debug("evaluateTriggers called ...");
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        logger.debug("requiredActionChallenge called ...");

        UserModel user = context.getUser();
        String mobileNumber = UserAttributeUtils.getMobileNumber(user);

        Response challenge = context.form()
                .setAttribute(Constants.ANSW_MOBILE_NUMBER, mobileNumber)
                .createForm(Constants.FORM_MOBILE_NUMBER);
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        logger.debug("processAction called ...");

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey(Constants.ANSW_CANCEL_CHANGE_MOBILE_NUMBER)) {
            context.getAuthenticationSession().addRequiredAction(VerifyMobileNumberRequiredActionFactory.PROVIDER_ID);
            context.success();
            return;
        }

        String providedMobileNumber = formData.getFirst(Constants.ANSW_MOBILE_NUMBER);
        if (MobileNumberUtils.validate(providedMobileNumber)) {
            logger.info("Valid matching mobile numbers supplied, save credential ...");

            UserAttributeUtils.setMobileNumber(context.getUser(), providedMobileNumber);

            context.getAuthenticationSession().addRequiredAction(VerifyMobileNumberRequiredActionFactory.PROVIDER_ID);

            context.success();
        } else {
            logger.info("The field wasn\'t complete or is an invalid number...");
            Response challenge = context.form()
                    .setError(Constants.MESSAGE_MOBILE_NUMBER_NO_VALID)
                    .createForm(Constants.FORM_MOBILE_NUMBER);
            context.challenge(challenge);
        }
    }

    @Override
    public void close() {
        logger.debug("close called ...");
    }
}
