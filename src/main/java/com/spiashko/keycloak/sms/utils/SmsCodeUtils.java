package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import com.spiashko.keycloak.sms.credprovider.SmsCredentialProvider;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Random;

public class SmsCodeUtils {

    private static boolean validateSmsCode(HttpRequest httpRequest, KeycloakSession session, RealmModel realm, UserModel user) {
        MultivaluedMap<String, String> formData = httpRequest.getDecodedFormParameters();
        String smsCode = formData.getFirst(Constants.ANSW_SMS_CODE);
        UserCredentialModel input = getUserCredentialModel(smsCode);
        return session.userCredentialManager().isValid(realm, user, input);
    }

    private static UserCredentialModel getUserCredentialModel(String code) {
        UserCredentialModel credentials = new UserCredentialModel();
        credentials.setType(SmsCredentialProvider.SMS_CODE_CRED_TYPE);
        credentials.setValue(code);
        return credentials;
    }

    public static boolean validateSmsCode(AuthenticationFlowContext context) {
        return validateSmsCode(context.getHttpRequest(), context.getSession(), context.getRealm(), context.getUser());
    }

    public static boolean validateSmsCode(RequiredActionContext context) {
        return validateSmsCode(context.getHttpRequest(), context.getSession(), context.getRealm(), context.getUser());
    }

    public static void storeSmsCode(AuthenticationFlowContext context, String code) {
        UserCredentialModel credentials = getUserCredentialModel(code);
        context.getSession().userCredentialManager().updateCredential(context.getRealm(), context.getUser(), credentials);
    }

    public static void storeSmsCode(RequiredActionContext context, String code) {
        UserCredentialModel credentials = getUserCredentialModel(code);
        context.getSession().userCredentialManager().updateCredential(context.getRealm(), context.getUser(), credentials);
    }

    public static String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
