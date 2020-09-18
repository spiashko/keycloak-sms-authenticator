package com.spiashko.keycloak.sms.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SmsPluginDiContainer {

    private static final Injector injector = Guice.createInjector();

    public static <T> T getInstance(Class<T> var1) {
        return injector.getInstance(var1);
    }

}
