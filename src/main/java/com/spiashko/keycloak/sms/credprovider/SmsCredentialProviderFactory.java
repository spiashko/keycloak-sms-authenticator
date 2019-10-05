package com.spiashko.keycloak.sms.credprovider;

import org.jboss.logging.Logger;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;

public class SmsCredentialProviderFactory implements CredentialProviderFactory<SmsCredentialProvider> {

    private static Logger logger = Logger.getLogger(SmsCredentialProviderFactory.class);

    @Override
    public String getId() {
        logger.debug("getId called ...");
        return "sms-code";
    }

    @Override
    public CredentialProvider create(KeycloakSession session) {
        logger.debug("create called ...");
        return new SmsCredentialProvider(session);
    }
}
