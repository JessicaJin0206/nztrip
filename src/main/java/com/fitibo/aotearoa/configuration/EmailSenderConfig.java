package com.fitibo.aotearoa.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by xiaozou on 8/17/16.
 */
@Configuration
public class EmailSenderConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "email")
//    public JavaMailSender javaMailSender() {
//        return new JavaMailSenderImpl();
//    }

}
