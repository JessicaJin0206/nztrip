package com.fitibo.aotearoa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
 * Created by qianhao.zhou on 10/11/2016.
 */
@Configuration
public class LocaleConfig {
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver result = new CookieLocaleResolver();
        result.setCookieName("language");
        result.setCookieMaxAge(Integer.MAX_VALUE);
        result.setDefaultLocale(Locale.ENGLISH);
        return result;
    }
}
