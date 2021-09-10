package com.ddinteractjava.controllers;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.model.OauthToken;
import com.ddinteractjava.services.FHIRService;
import com.ddinteractjava.services.RestTemplateService;
import com.ddinteractjava.services.SessionCacheService;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
public class LaunchController {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private RestTemplateService restTemplateService;

    @Autowired
    private SessionCacheService sessionCacheService;

    private final Map<String, FHIRService> fhirServiceMap;

    LaunchController(Map<String, FHIRService> fhirServiceMap) {
        this.fhirServiceMap = fhirServiceMap;
    }

    private Map<String, String> oAuthUrls = new HashMap<>();

    @RequestMapping(value = "/launchStandalone", method = RequestMethod.GET)
    public String launchStandalone(@RequestParam(name = "ddi", required = true) String ddi, HttpServletRequest
            httpServletRequest, HttpServletResponse response) throws IOException {
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();

        /**
         * First time through on the server, get the conformance information.
         */
        if (oAuthUrls.isEmpty()) {
            loadOAuthUrls(appConfig.getFhirUrl(), appConfig.getAud());
        }
        if (appConfig.getAuthorizeUrl() == null) {
            appConfig.setAuthorizeUrl(oAuthUrls.get("authorize"));
        }

        if (appConfig.getTokenUrl() == null) {
            appConfig.setTokenUrl(oAuthUrls.get("token"));
        }


        String patient = requestParameterMap.get("patient")[0];
        String state;
        if (appConfig.getStaticToken() != null) {
            state = appConfig.getStaticToken();
        } else {
            state = requestParameterMap.get("state")[0];
        }
        String accessToken = appConfig.getStaticToken();
        try {

            //I am replacing the access token and state with the static bearer token
            OauthToken token = new OauthToken(oAuthUrls.get("serviceUrl"),
                    accessToken,
                    state,
                    patient,
                    new Date()
            );

            sessionCacheService.addTokenToCache(state, httpServletRequest.getRemoteAddr(), token);

            // Set an authentication.
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(state, accessToken, new ArrayList<>()));

            String redirectUrl = httpServletRequest.getContextPath() + "/" + ddi + "?state=" + state;

            response.sendRedirect(redirectUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping(value = "/launch", method = RequestMethod.GET)
    public String launch(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();

        String client_id = appConfig.getClientId();
        String redirect_url = appConfig.getRedirectUrl();
        String scope = appConfig.getScope();

        String state = UUID.randomUUID().toString();

        String launch = requestParameterMap.containsKey("launch") ? requestParameterMap.get("launch")[0] : "";
        String aud = requestParameterMap.containsKey("iss") ? requestParameterMap.get("iss")[0] : "";

        /**
         * First time through on the server, get the conformance information.
         */
        if (oAuthUrls.isEmpty()) {
            loadOAuthUrls(requestParameterMap.get("iss")[0], aud);
        }
        if (appConfig.getAuthorizeUrl() == null) {
            appConfig.setAuthorizeUrl(oAuthUrls.get("authorize"));
        }

        if (appConfig.getTokenUrl() == null) {
            appConfig.setTokenUrl(oAuthUrls.get("token"));
        }


        try {
            String authUrl = appConfig.getAuthorizeUrl() + "?" + String.join("&",
                    "client_id=" + URLEncoder.encode(client_id, "UTF-8"),
                    "response_type=code",
                    "redirect_uri=" + URLEncoder.encode(redirect_url, "UTF-8"),
                    "scope=" + URLEncoder.encode(scope, "UTF-8"),
                    "state=" + URLEncoder.encode(state, "UTF-8"),
                    "launch=" + URLEncoder.encode(launch, "UTF-8"),
                    "aud=" + URLEncoder.encode(aud, "UTF-8"));
//            System.out.println(authUrl);
            response.sendRedirect(authUrl);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirect(@RequestParam(name = "ddi", required = true) String ddi, HttpServletRequest
            httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Map<String, String[]> requestParameterMap = httpServletRequest.getParameterMap();
        String tokenUrl = appConfig.getTokenUrl();
        try {
            String code = URLEncoder.encode(requestParameterMap.get("code")[0], "ASCII");

            RestTemplate restTemplate = restTemplateService.getRestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("grant_type", "authorization_code");
            map.add("redirect_uri", appConfig.getRedirectUrl());
            map.add("client_id", appConfig.getClientId());

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(tokenUrl,
                            HttpMethod.POST,
                            entity,
                            String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                Map tokenData = appConfig.gson().fromJson(response.getBody(), Map.class);
                OauthToken token = new OauthToken(oAuthUrls.get("serviceUrl"),
                        (String) tokenData.get("access_token"),
                        requestParameterMap.get("state")[0],
                        (String) tokenData.get("patient"),
                        new Date());

                if (appConfig.getStaticToken() == null) {
//                    System.out.println("Bearer token: " + tokenData.get("access_token"));
                    fhirServiceMap.get(appConfig.getFhirVersion()).addBearerToken((String) tokenData.get("access_token"));
                }
                sessionCacheService.addTokenToCache(requestParameterMap.get("state")[0], httpServletRequest.getRemoteAddr(), token);

                // Set an authentication.
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(requestParameterMap.get("state"), tokenData.get("access_token"), new ArrayList<>()));

                //TODO Need to figure out a way to navigate to correct controller for drug-drug interaction
                String redirectUrl = httpServletRequest.getContextPath() + "/" + ddi + "?state=" + requestParameterMap.get("state")[0];
                httpServletResponse.sendRedirect(redirectUrl);
            } else {
                return "error";
//                throw new Exception("No data.");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";

    }

    protected void loadOAuthUrls(String iss, String aud) {
        String conformanceUrl = null;

        if (iss.startsWith("/")) {
            conformanceUrl = aud + iss;
        } else {
            conformanceUrl = iss;
        }

        if (appConfig.getFhirVersion().equals("stu2")) {
            Conformance conformance = sessionCacheService.getDstu2ConformanceFromCache(conformanceUrl);

            if (conformance == null) {
                conformance = fhirServiceMap.get(appConfig.getFhirVersion()).getClient().capabilities().ofType(Conformance.class).execute();
            }

            /**
             * Has authorize and token keys.
             */
            oAuthUrls.putAll(readDstu2OAuth2Urls(conformance));
            for (Map.Entry<String, String> entry : oAuthUrls.entrySet()) {
                if (entry.getValue().startsWith("/")) {
                    oAuthUrls.put(entry.getKey(), aud + entry.getValue());
                }
            }

            oAuthUrls.put("serviceUrl", conformanceUrl);
            sessionCacheService.addDstu2ConformanceToCache(conformanceUrl, conformance);

        } else if (appConfig.getFhirVersion().equals("r4")) {
            CapabilityStatement conformance = sessionCacheService.getConformanceFromCache(conformanceUrl);

            if (conformance == null) {
                conformance = fhirServiceMap.get(appConfig.getFhirVersion()).getClient().capabilities().ofType(CapabilityStatement.class).execute();
            }

            /**
             * Has authorize and token keys.
             */
            oAuthUrls.putAll(readOAuth2Urls(conformance));
            for (Map.Entry<String, String> entry : oAuthUrls.entrySet()) {
                if (entry.getValue().startsWith("/")) {
                    oAuthUrls.put(entry.getKey(), aud + entry.getValue());
                }
            }

            if (conformance.getImplementation().hasUrl() && !conformance.getImplementation().getUrl().endsWith("metadata")) {
                oAuthUrls.put("serviceUrl", conformance.getImplementation().getUrl());
            } else {
                oAuthUrls.put("serviceUrl", conformanceUrl);
            }
            sessionCacheService.addConformanceToCache(conformanceUrl, conformance);
        }

    }

    /**
     * Get the authorize and token urls from the conformance statement.
     *
     * @param metadata The service conformance.
     * @return A map with two keys, authorize and token that have the respective urls.
     */
    protected Map readOAuth2Urls(CapabilityStatement metadata) {
        Map<String, String> conformance = new HashMap();

        //For OMOPonFHIR the authorize and token URLs are located here
        List<Extension> extensionList = metadata.getRest().get(0).getSecurity().getExtension().get(0).getExtension();
        for (Extension extension : extensionList) {
            if (extension.getUrl().equals("authorize") || extension.getUrl().equals("token")) {
                conformance.put(extension.getUrl(), ((PrimitiveType) extension.getValue()).asStringValue());
            }
        }
        return conformance;
    }

    protected Map readDstu2OAuth2Urls(Conformance metadata) {
        Map<String, String> conformance = new HashMap();

        //For OMOPonFHIR the authorize and token URLs are located here
        List<ExtensionDt> extensionDtList = metadata.getRest().get(0).getSecurity().getUndeclaredExtensions();
        for (ExtensionDt extension : extensionDtList) {
            if (extension.getUrl().equals("authorize") || extension.getUrl().equals("token")) {
                conformance.put(extension.getUrl(), ((PrimitiveType) extension.getValue()).asStringValue());
            }
        }
        return conformance;
    }


}
