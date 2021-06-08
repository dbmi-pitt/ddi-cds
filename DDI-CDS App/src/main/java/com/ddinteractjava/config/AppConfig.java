package com.ddinteractjava.config;


import com.ddinteractjava.services.R4Service;
import com.ddinteractjava.services.STU2Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String fhirVersion;

    private String clientId;

    private String scope;

    private String aud;

    private String redirectUrl;

    private String authorizeUrl;

    private String tokenUrl;

    private String fhirUrl;

    private String staticToken;

    private String clientSecret;

    private String appEntryPoint;

    private String cdsService;

    private String practitioner;

    private String vsacApiKey;

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getFhirUrl() {
        return fhirUrl;
    }

    public void setFhirUrl(String fhirUrl) {
        this.fhirUrl = fhirUrl;
    }

    public String getStaticToken() {
        return staticToken;
    }

    public void setStaticToken(String staticToken) {
        this.staticToken = staticToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAppEntryPoint() {
        return appEntryPoint;
    }

    public void setAppEntryPoint(String appEntryPoint) {
        this.appEntryPoint = appEntryPoint;
    }

    public String getCdsService() {
        return cdsService;
    }

    public void setCdsService(String cdsService) {
        this.cdsService = cdsService;
    }

    public String getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(String practitioner) {
        this.practitioner = practitioner;
    }

    public String getVsacApiKey() {
        return vsacApiKey;
    }

    public void setVsacApiKey(String vsacApiKey) {
        this.vsacApiKey = vsacApiKey;
    }

    @Bean
    public SimpleCacheManager simpleCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<>();
        Cache oauthTokenCache = new ConcurrentMapCache("oauthTokenCache");
        Cache conformanceCache = new ConcurrentMapCache("conformanceCache");
        caches.add(oauthTokenCache);
        caches.add(conformanceCache);
        simpleCacheManager.setCaches(caches);
        return simpleCacheManager;
    }

    @Bean
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setPrettyPrinting().create();
    }

    @Bean("stu2")
    STU2Service stu2Service() {
        return new STU2Service();
    }

    @Bean("r4")
    R4Service r4Service() {
        return new R4Service();
    }

}
