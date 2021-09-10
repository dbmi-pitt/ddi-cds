package com.ddinteractjava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:cyp1a2.properties")
@Configuration
public class CYP1A2Config {

    @Value("${cyp1a2.vsac_tizanidine}")
    private String vsacTizanidine;

    @Value("${cyp1a2.vsac_cyp1a2}")
    private String vsacCYP1A2;

    public String getVsacTizanidine() {
        return vsacTizanidine;
    }

    public String getVsacCYP1A2() {
        return vsacCYP1A2;
    }

}
