package com.spiashko.keycloak.sms.authenticator;

import com.google.inject.Inject;
import com.spiashko.keycloak.sms.service.AuthenticationFlowContextSmsAdapter;
import com.spiashko.keycloak.sms.service.SmsService;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.inject.Singleton;

@Singleton
public class SmsAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger(SmsAuthenticator.class);

    private final SmsService smsService;

    @Inject
    public SmsAuthenticator(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        logger.debug("authenticate called ...");
        smsService.smsChallenge(new AuthenticationFlowContextSmsAdapter(context));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        logger.debug("action called ...");
        smsService.verifySmsChallenge(new AuthenticationFlowContextSmsAdapter(context));
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
