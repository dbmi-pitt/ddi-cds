package com.ddi_cds_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:cyp3a4.properties")
@Configuration
public class CYP3A4Config {

    @Value("${cyp3a4.riskfactor.aki}")
    private String AKI;

    @Value("${cyp3a4.riskfactor.ckd}")
    private String CKD;

    @Value("${cyp3a4.riskfactor.egfr}")
    private String eGFR;

    @Value("${cyp3a4.vsac_colchicine}")
    private String vsacColchicine;

    @Value("${cyp3a4.vsac_cyp3a4}")
    private String vsacCYP3A4;

    public String getAKI() {
        return AKI;
    }

    public String getCKD() {
        return CKD;
    }

    public String geteGFR() {
        return eGFR;
    }

    public String getVsacColchicine() {
        return vsacColchicine;
    }

    public String getVsacCYP3A4() {
        return vsacCYP3A4;
    }

}
