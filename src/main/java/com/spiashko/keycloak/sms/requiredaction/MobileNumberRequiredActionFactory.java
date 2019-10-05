package com.spiashko.keycloak.sms.requiredaction;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class MobileNumberRequiredActionFactory implements RequiredActionFactory {

    public static final String PROVIDER_ID = "sms_auth_check_mobile";

    private static Logger logger = Logger.getLogger(MobileNumberRequiredActionFactory.class);
    private static final MobileNumberRequiredAction SINGLETON = new MobileNumberRequiredAction();

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        logger.debug("create called ...");
        return SINGLETON;
    }

    @Override
    public String getId() {
        logger.debug("getId called ... returning " + PROVIDER_ID);
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        logger.debug("getDisplayText called ...");
        return "Update Mobile Number";
    }

    @Override
    public void init(Config.Scope config) {
        logger.debug("init called ...");
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        logger.debug("postInit called ...");
    }

    @Override
    public void close() {
        logger.debug("close called ...");
    }
}
