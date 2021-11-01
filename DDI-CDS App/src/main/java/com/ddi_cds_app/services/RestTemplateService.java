package com.ddi_cds_app.services;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * Service for getting Rest Templates. This service ignores SSL Certificate errors if running with profile
 * development or test.
 */

public interface RestTemplateService {

    /**
     * Get a rest template. This ignores SSL Certificate errors if running with profile
     * development or test.
     *
     * @return a RestTemplate that ignores SSL Certificate errors if running with profile development or test.
     */
    @Bean
    RestTemplate getRestTemplate();
}
