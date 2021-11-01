package com.ddi_cds_app.services;

import com.ddi_cds_app.config.AppConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class VSACService {
    @Autowired
    private AppConfig appConfig;

    public void buildCodeList(String response, List<String> codeList) {
        if (response != null) {
            JsonObject valueset = JsonParser.parseString(response).getAsJsonObject();
            JsonArray codeArray = valueset.get("compose").getAsJsonObject().get("include").getAsJsonArray().get(0).getAsJsonObject().get("concept").getAsJsonArray();
            for (JsonElement codeObject : codeArray) {
                codeList.add(codeObject.getAsJsonObject().get("code").getAsString());
            }
        }
    }

    public String callVSAC(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String auth = "apikey:" + appConfig.getVsacApiKey();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
        String authHeaderValue = "Basic " + new String(encodedAuth);

        con.setRequestProperty("Authorization", authHeaderValue);
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        int status = con.getResponseCode();
        if (status == 200) {

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } else {
            return null;
        }
    }
}
