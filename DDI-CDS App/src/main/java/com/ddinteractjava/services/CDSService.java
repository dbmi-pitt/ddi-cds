package com.ddinteractjava.services;

import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.model.CDSPostBody;
import com.ddinteractjava.model.FhirAuthorization;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CDSService {
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private R4Service r4Service;

    final JsonParser parser = new JsonParser();

    public String callPatientView(String accessToken, String subject, String patientId) throws IOException {
        // Responsible for make CDS service calls and handling the cards returned
        URL url = new URL(appConfig.getCdsService());

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json+fhir");

        con.setDoOutput(true);

        //JSON String need to be constructed for the specific resource.
        //We may construct complex JSON using any third-party JSON libraries such as jackson or org.json
        String jsonInputString = createPostBody(accessToken, subject, patientId);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        System.out.println(code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String createPostBody(String accessToken, String subject, String patientId) {
        CDSPostBody cdsPostBody = new CDSPostBody();
        FhirAuthorization fhirAuthorization = new FhirAuthorization();
        fhirAuthorization.setAccess_token(accessToken);
        fhirAuthorization.setToken_type("Bearer");
        fhirAuthorization.setExpires_in("500");
        fhirAuthorization.setScope(appConfig.getScope());
        fhirAuthorization.setSubject(subject);
        cdsPostBody.setFhirAuthorization(fhirAuthorization);

        cdsPostBody.setHookInstance(UUID.randomUUID().toString());
        cdsPostBody.setHook("patient-view");
        cdsPostBody.setFhirServer(appConfig.getFhirUrl());
        cdsPostBody.setPatientId(patientId);
        cdsPostBody.setUserId(appConfig.getPractitioner());

        Map<String, String> context = new HashMap<>();
        context.put("userId", appConfig.getPractitioner());
        context.put("patientId", patientId);
        cdsPostBody.setContext(context);

        String prefetch = r4Service.getPrefetch(patientId);
        cdsPostBody.setPrefetch(parser.parse(prefetch));

        String json = appConfig.gson().toJson(cdsPostBody);
        return json;
    }


}
