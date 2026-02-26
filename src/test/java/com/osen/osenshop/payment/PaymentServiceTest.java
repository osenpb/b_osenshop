package com.osen.osenshop.payment;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceTest.class);

    @Test
    void verifyAccessTokenInjected() {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        Properties properties = yaml.getObject();

        assert properties != null;
        String accessToken = properties.getProperty("mercadopago.access-token");
        
        log.info("AccessToken value: {}", accessToken);
        assertNotNull(accessToken, "El access token no fue inyectado");
    }

}
