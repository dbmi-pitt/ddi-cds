package com.ddi_cds_app.model;

import com.google.gson.JsonElement;

import java.util.Map;

public class CDSPostBody {
    String hookInstance;
    String hook;
    String fhirServer;
    FhirAuthorization fhirAuthorization;
    String userId;
    String patientId;
    JsonElement prefetch;
    Map<String, String> context;

    public CDSPostBody() {
    }

    public CDSPostBody(String hook, String fhirServer, FhirAuthorization fhirAuthorization, String userId, String patientId, JsonElement prefetch) {
        this.hook = hook;
        this.fhirServer = fhirServer;
        this.fhirAuthorization = fhirAuthorization;
        this.userId = userId;
        this.patientId = patientId;
        this.prefetch = prefetch;
        this.context.put("patientId", patientId);
        this.context.put("userId", userId);
    }

    public String getHookInstance() {
        return hookInstance;
    }

    public void setHookInstance(String hookInstance) {
        this.hookInstance = hookInstance;
    }

    public String getHook() {
        return hook;
    }

    public void setHook(String hook) {
        this.hook = hook;
    }

    public String getFhirServer() {
        return fhirServer;
    }

    public void setFhirServer(String fhirServer) {
        this.fhirServer = fhirServer;
    }

    public FhirAuthorization getFhirAuthorization() {
        return fhirAuthorization;
    }

    public void setFhirAuthorization(FhirAuthorization fhirAuthorization) {
        this.fhirAuthorization = fhirAuthorization;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public JsonElement getPrefetch() {
        return prefetch;
    }

    public void setPrefetch(JsonElement prefetch) {
        this.prefetch = prefetch;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }
}
