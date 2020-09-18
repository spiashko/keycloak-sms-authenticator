package com.spiashko.keycloak.sms.service;

import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.sessions.AuthenticationSessionModel;

import javax.ws.rs.core.Response;

@RequiredArgsConstructor
public class AuthenticationFlowContextSmsAdapter implements SmsContext {

    private final AuthenticationFlowContext context;

    @Override
    public HttpRequest getHttpRequest() {
        return context.getHttpRequest();
    }

    @Override
    public KeycloakSession getSession() {
        return context.getSession();
    }

    @Override
    public UserModel getUser() {
        return context.getUser();
    }

    @Override
    public AuthenticationSessionModel getAuthenticationSession() {
        return context.getAuthenticationSession();
    }

    @Override
    public LoginFormsProvider form() {
        return context.form();
    }

    @Override
    public void success() {
        context.success();
    }

    @Override
    public void challenge(Response response) {
        context.challenge(response);
    }
}
