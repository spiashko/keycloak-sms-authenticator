package com.spiashko.keycloak.sms.credprovider;

import org.jboss.logging.Logger;
import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.cache.CachedUserModel;
import org.keycloak.models.cache.OnUserCache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmsCredentialProvider implements CredentialProvider, CredentialInputValidator, CredentialInputUpdater, OnUserCache {

    private static Logger logger = Logger.getLogger(SmsCredentialProvider.class);

    public static final String SMS_CODE_CRED_TYPE = "SMS_CODE";
    public static final String CACHE_KEY = SmsCredentialProvider.class.getName() + "." + SMS_CODE_CRED_TYPE;

    protected KeycloakSession session;

    public SmsCredentialProvider(KeycloakSession session) {
        logger.debug("SmsCredentialProvider called ...");
        this.session = session;
    }

    public CredentialModel getSmsCode(RealmModel realm, UserModel user) {
        CredentialModel smsCode = null;
        if (user instanceof CachedUserModel) {
            CachedUserModel cached = (CachedUserModel) user;
            smsCode = (CredentialModel) cached.getCachedWith().get(CACHE_KEY);

        } else {
            List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_CODE_CRED_TYPE);
            if (!creds.isEmpty()) smsCode = creds.get(0);
        }
        return smsCode;
    }


    @Override
    public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
        logger.debug("updateCredential called ...");

        if (!SMS_CODE_CRED_TYPE.equals(input.getType())) return false;
        if (!(input instanceof UserCredentialModel)) return false;
        UserCredentialModel credInput = (UserCredentialModel) input;
        List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_CODE_CRED_TYPE);
        if (creds.isEmpty()) {
            CredentialModel smsCode = new CredentialModel();
            smsCode.setType(SMS_CODE_CRED_TYPE);
            smsCode.setValue(credInput.getValue());
            smsCode.setCreatedDate(Time.currentTimeMillis());
            session.userCredentialManager().createCredential(realm, user, smsCode);
        } else {
            creds.get(0).setValue(credInput.getValue());
            session.userCredentialManager().updateCredential(realm, user, creds.get(0));
        }
        session.userCache().evict(realm, user);
        return true;
    }

    @Override
    public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
        logger.debug("disableCredentialType called ...");

        if (!SMS_CODE_CRED_TYPE.equals(credentialType)) return;

        List<CredentialModel> credentials = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_CODE_CRED_TYPE);
        for (CredentialModel cred : credentials) {
            session.userCredentialManager().removeStoredCredential(realm, user, cred.getId());
        }
        session.userCache().evict(realm, user);
    }

    @Override
    public Set<String> getDisableableCredentialTypes(RealmModel realm, UserModel user) {
        logger.debug("getDisableableCredentialTypes called ...");

        if (!session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_CODE_CRED_TYPE).isEmpty()) {
            Set<String> set = new HashSet<>();
            set.add(SMS_CODE_CRED_TYPE);
            return set;
        } else {
            return Collections.EMPTY_SET;
        }

    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        logger.debug("supportsCredentialType called ...");

        return SMS_CODE_CRED_TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        logger.debug("isConfiguredFor called ...");

        if (!SMS_CODE_CRED_TYPE.equals(credentialType)) return false;
        return getSmsCode(realm, user) != null;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
        logger.debug("isValid called ...");

        if (!SMS_CODE_CRED_TYPE.equals(input.getType())) return false;
        if (!(input instanceof UserCredentialModel)) return false;

        String smsCode = getSmsCode(realm, user).getValue();

        return smsCode != null && ((UserCredentialModel) input).getValue().equals(smsCode);
    }

    @Override
    public void onCache(RealmModel realm, CachedUserModel user, UserModel delegate) {
        logger.debug("onCache called ...");

        List<CredentialModel> creds = session.userCredentialManager().getStoredCredentialsByType(realm, user, SMS_CODE_CRED_TYPE);
        if (!creds.isEmpty()) user.getCachedWith().put(CACHE_KEY, creds.get(0));
    }
}
