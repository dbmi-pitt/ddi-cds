package com.ddi_cds_app.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RxNormService {
    @Autowired
    private VSACService vsacService;

    private String apiUrl = "https://rxnav.nlm.nih.gov/REST/rxcui/";
    private String imageUrl = "https://rximage.nlm.nih.gov/api/rximage/1/rxnav?rxcui=";

    public String callRxNorm(String rxCui) throws IOException {
        // We want to call the RxNorm API with a CUI and then determine the ingredient
        URL url = new URL(apiUrl + rxCui + "/related.json?tty=IN");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

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

    public String getImageUrl(String rxCui) {
        try {
            URL url = new URL(apiUrl + rxCui);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

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

                JsonObject nlmRxImage = JsonParser.parseString(response.toString()).getAsJsonObject().get("nlmRxImages").getAsJsonArray().get(0).getAsJsonObject();
                return nlmRxImage.get("imageUrl").getAsString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
