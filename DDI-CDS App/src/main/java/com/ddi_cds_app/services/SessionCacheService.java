package com.ddi_cds_app.services;

import ca.uhn.fhir.model.dstu2.resource.Conformance;
import com.ddi_cds_app.model.OauthToken;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;

@Service
public class SessionCacheService {
    private static String OAUTH_TOKEN_CACHE_NAME = "oauthTokenCache";

    private static String CONFORMANCE_CACHE_NAME = "conformanceCache";

    private static int MINUTES_TOKEN_IS_VALID = 60;

    org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SimpleCacheManager simpleCacheManager;

    public void addDstu2ConformanceToCache(String state, Conformance conformance) {
        Assert.notNull(state, "state cannot be null.");
        Assert.notNull(conformance, "conformance cannot be null.");
        simpleCacheManager.getCache(CONFORMANCE_CACHE_NAME).put(state, conformance);
    }

    public Conformance getDstu2ConformanceFromCache(String serviceUrl) {
        Assert.notNull(serviceUrl, "serviceUrl cannot be null.");
        Cache.ValueWrapper wrapper = simpleCacheManager.getCache(CONFORMANCE_CACHE_NAME).get(serviceUrl);
        if (wrapper == null) {
            return null;
        } else {
            return (Conformance) wrapper.get();
        }
    }


    public void addConformanceToCache(String state, CapabilityStatement conformance) {
        Assert.notNull(state, "state cannot be null.");
        Assert.notNull(conformance, "conformance cannot be null.");
        simpleCacheManager.getCache(CONFORMANCE_CACHE_NAME).put(state, conformance);
    }

    public CapabilityStatement getConformanceFromCache(String serviceUrl) {
        Assert.notNull(serviceUrl, "serviceUrl cannot be null.");
        Cache.ValueWrapper wrapper = simpleCacheManager.getCache(CONFORMANCE_CACHE_NAME).get(serviceUrl);
        if (wrapper == null) {
            return null;
        } else {
            return (CapabilityStatement) wrapper.get();
        }
    }

    public void addTokenToCache(String state, String ipAddress, OauthToken token) {
        Assert.notNull(state, "state cannot be null.");
        Assert.notNull(ipAddress, "IP Address cannot be null.");
        Assert.notNull(token, "token cannot be null.");
        simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME).put(state, token);
    }

    public OauthToken getTokenFromCache(String state, String ipAddress) {
        Assert.notNull(state, "state cannot be null.");
        Assert.notNull(ipAddress, "IP Address cannot be null.");
        OauthToken token = (OauthToken) simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME).get(state).get();

        if (new Date().after(new Date(token.getCreated().getTime() + MINUTES_TOKEN_IS_VALID * 60 * 1000))) {
            simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME).evict(state);
            return null;
        }
        return token;
    }

    //  Have an expiration job that runs hourly to clear token cache.
    @Scheduled(fixedRate = 60000L)
    public void expireOauthTokens() {
        Set<Object> keys = ((ConcurrentMapCache) simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME)).getNativeCache().keySet();
        log.debug("Expiring oauthTokens...");
        for (Object key : keys) {
            Cache.ValueWrapper token = simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME).get(key);
            if (new Date().after(new Date(((OauthToken) token).getCreated().getTime() + MINUTES_TOKEN_IS_VALID * 60 * 1000))) {
                simpleCacheManager.getCache(OAUTH_TOKEN_CACHE_NAME).evict(key);
            }
        }
    }
}
