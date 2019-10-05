package com.spiashko.keycloak.sms.utils;

import com.spiashko.keycloak.sms.Constants;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.common.util.ServerCookie;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import java.net.URI;

public class CookieUtils {

    public static final String REALMS_PATH = "realms";

    public static void setCookie(RequiredActionContext context) {
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        URI uri = context.getUriInfo().getBaseUriBuilder().path(REALMS_PATH).path(context.getRealm().getName()).build();
        addCookie(Constants.COOKIE_SMS_CODE_PROVIDED, Boolean.TRUE.toString(),
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    public static void setCookie(AuthenticationFlowContext context) {
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        URI uri = context.getUriInfo().getBaseUriBuilder().path(REALMS_PATH).path(context.getRealm().getName()).build();
        addCookie(Constants.COOKIE_SMS_CODE_PROVIDED, Boolean.TRUE.toString(),
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    public static void addCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = ResteasyProviderFactory.getContextData(HttpResponse.class);
        StringBuffer cookieBuf = new StringBuffer();
        ServerCookie.appendCookieValue(cookieBuf, 1, name, value, path, domain, comment, maxAge, secure, httpOnly);
        String cookie = cookieBuf.toString();
        response.getOutputHeaders().add(HttpHeaders.SET_COOKIE, cookie);
    }

    public static boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get(Constants.COOKIE_SMS_CODE_PROVIDED);
        return cookie != null;
    }
}
