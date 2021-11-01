package com.ddi_cds_app.cyp1a2;

import com.ddi_cds_app.config.CYP1A2Config;
import com.ddi_cds_app.services.RxNormService;
import com.ddi_cds_app.services.VSACService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CYP1A2Cache {
    @Autowired
    private CYP1A2Config cyp1A2Config;

    @Autowired
    private VSACService vsacService;

    @Autowired
    private RxNormService rxNormService;

    //Get filled by the VSAC Service
    public List<String> tizanidineCodes = new ArrayList<>();
    public List<String> cyp1a2Codes = new ArrayList<>();

    //RxNorm codes for CYP1A2 inhibitors
    private final String ciprofloxacin = "2551";
    private final String fluvoxamine = "42355";
    private final String zafirlukast = "114970";

    private final String ethinylEstradiol = "4124";
    private final String mexiletine = "6926";
    private final String phenylpropanolamine = "8175";
    private final String pipemidicAcid = "8337";
    private final String piperine = "1311146";
    private final String rofecoxib = "232158";
    private final String vemurafenib = "1147220";
    private final String zileuton = "40575";
    private final String enoxacin = "3925";
    private final String acyclovir = "281";
    private final String allopurinol = "519";
    private final String cimetidine = "2541";
    private final String peginterferonAlfa2A = "120608";

    //Gets filled by the RxNorm Service
    public List<String> ciprofloxacinAlternatives = new ArrayList<>();
    public List<String> fluvoxamineAlternatives = new ArrayList<>();
    public List<String> zafirlukastAlternatives = new ArrayList<>();


    public void createCYP1A1Cache() throws IOException {
        // Will call the VSAC API and make a local reference of the ValueSets
        String response = vsacService.callVSAC(cyp1A2Config.getVsacTizanidine());
        vsacService.buildCodeList(response, tizanidineCodes);

        response = vsacService.callVSAC(cyp1A2Config.getVsacCYP1A2());
        vsacService.buildCodeList(response, cyp1a2Codes);


        for (String code : cyp1a2Codes) {
            addToCache(code);
        }
    }


    private void addToCache(String rxCui) throws IOException {
        String response = rxNormService.callRxNorm(rxCui);
        try {
            JsonArray concepts = JsonParser.parseString(response).getAsJsonObject().get("relatedGroup").getAsJsonObject().get("conceptGroup").getAsJsonArray().get(0).getAsJsonObject().get("conceptProperties").getAsJsonArray();
            for (JsonElement concept : concepts) {
                String ingredientCui = concept.getAsJsonObject().get("rxcui").getAsString();
                switch (ingredientCui) {
                    case ciprofloxacin:
                        ciprofloxacinAlternatives.add(rxCui);
                        break;
                    case fluvoxamine:
                        fluvoxamineAlternatives.add(rxCui);
                        break;
                    case zafirlukast:
                        zafirlukastAlternatives.add(rxCui);
                        break;
                    default:
                        break;

                }
            }
        } catch (Exception e) {
            System.out.println(rxCui);
            System.out.println(response);
            // Can't find the RxCui so we cannot determine the ingredient group the drug belongs to
        }
    }
}
