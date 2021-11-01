package com.ddi_cds_app.model;

import java.util.Date;

public class OauthToken {
    String serviceUrl;
    String accessToken;
    String state;
    String patient;
    Date created = new Date();

    public OauthToken(String serviceUrl, String accessToken, String state, String patient, Date created) {
        this.serviceUrl = serviceUrl;
        this.accessToken = accessToken;
        this.state = state;
        this.patient = patient;
        this.created = created;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
