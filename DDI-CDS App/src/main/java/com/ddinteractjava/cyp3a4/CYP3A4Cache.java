package com.ddinteractjava.cyp3a4;

import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.config.CYP3A4Config;
import com.ddinteractjava.services.RxNormService;
import com.ddinteractjava.services.VSACService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CYP3A4Cache {
    @Autowired
    private CYP3A4Config cyp3A4Config;

    @Autowired
    private VSACService vsacService;

    @Autowired
    private RxNormService rxNormService;

    //Get filled by the VSAC Service
    public List<String> colchicineCodes = new ArrayList<>();
    public List<String> cyp3a4Codes = new ArrayList<>();

    private final String ritonavir = "85762";
    private final String telaprevir = "1102261";
    private final String indinavir = "114289";
    private final String nelfinavir = "134527";
    private final String saquinavir = "83395";
    private final String boceprevir = "1102129";
    private final String darunavir = "460132";
    private final String atazanavir = "343047";
    private final String amprenavir = "228656";
//    private final String faldaprevir;

    private final String itraconazoleCui = "28031";
    private final String ketoconazoleCui = "6135";
    private final String posaconazoleCui = "282446";
    private final String voriconazoleCui = "121243";
    private final String clarithromycinCui = "21212";
    private final String nefazodoneCui = "31565";
    private final String erythromycinCui = "4053";
    private final String diltiazemCui = "3443";
    private final String verapamilCui = "11170";
    private final String dronedaroneCui = "233698";

    //Gets fills by the RxNorm Service
    public List<String> discontinueColchicine = new ArrayList<>();
    public List<String> itraconazoleKetoconazolePosaconazole = new ArrayList<>();
    public List<String> voriconazole = new ArrayList<>();
    public List<String> clarithromycin = new ArrayList<>();
    public List<String> nefazodone = new ArrayList<>();
    public List<String> erythromycin = new ArrayList<>();
    public List<String> diltiazemVerapamil = new ArrayList<>();
    public List<String> dronedarone = new ArrayList<>();

    public void createCYP3A4Cache() throws IOException {
        // Will call the VSAC API and make a local reference of the ValueSets
        String response = vsacService.callVSAC(cyp3A4Config.getVsacColchicine());
        vsacService.buildCodeList(response, colchicineCodes);

        response = vsacService.callVSAC(cyp3A4Config.getVsacCYP3A4());
        vsacService.buildCodeList(response, cyp3a4Codes);


        for (String code : cyp3a4Codes) {
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
                    case ritonavir:
                    case telaprevir:
                    case indinavir:
                    case nelfinavir:
                    case saquinavir:
                    case boceprevir:
                    case darunavir:
                    case atazanavir:
                    case amprenavir:
                        discontinueColchicine.add(rxCui);
                        break;
                    case itraconazoleCui:
                    case ketoconazoleCui:
                    case posaconazoleCui:
                        itraconazoleKetoconazolePosaconazole.add(rxCui);
                        break;
                    case voriconazoleCui:
                        voriconazole.add(rxCui);
                        break;
                    case clarithromycinCui:
                        clarithromycin.add(rxCui);
                        break;
                    case nefazodoneCui:
                        nefazodone.add(rxCui);
                        break;
                    case diltiazemCui:
                    case verapamilCui:
                        diltiazemVerapamil.add(rxCui);
                        break;
                    case dronedaroneCui:
                        dronedarone.add(rxCui);
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
