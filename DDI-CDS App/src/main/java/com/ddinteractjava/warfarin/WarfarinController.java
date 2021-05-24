package com.ddinteractjava.warfarin;


import com.ddinteractjava.model.OauthToken;
import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.services.CDSService;
import com.ddinteractjava.services.R4Service;
import com.ddinteractjava.services.SessionCacheService;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
public class WarfarinController {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private R4Service r4Service;

    @Autowired
    SessionCacheService sessionCacheService;

    @Autowired
    CDSService cdsService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test(@RequestParam(name = "state", required = true) String state, HttpServletRequest httpServletRequest) throws IOException {
        ModelAndView model = new ModelAndView("ddinteract");

        OauthToken token = sessionCacheService.getTokenFromCache(state, httpServletRequest.getRemoteAddr());

//        String card = cdsService.callPatientView(token.getAccessToken(), appConfig.getClientId(), token.getPatient());
//        model.addObject("card", card);

        Map<String, String> patientProfiles = new HashMap<>();
        patientProfiles.put("1497", "Born in 1970 and taking\n" +
                "warfarin 4 MG tablet, sulindac 200 MG tablet, and spironolactone 100 MG tablet");
        patientProfiles.put("1496", "Born in 1952 and taking\n" +
                "warfarin 4 MG tablet and sulindac 200 MG tablet with a history of GI bleeding");
        patientProfiles.put("1498", "Born in 1940 and taking\n" +
                "warfarin 10 MG tablet, prednisone 20 MG tablet, and ketorolac tromethamine 10 MG tablet");
        patientProfiles.put("1499", "Born in 1970 and taking\n" +
                "warfarin 10 MG tablet and ketorolac tromethamine 10 MG tablet");
        patientProfiles.put("1565", "Born in 1952 and taking 4\n" +
                "MG warfarin tablet and a topical diclofenac lotion");
        model.addObject("patientProfiles", patientProfiles);

//        Map<String, String[]> riskFactors = new LinkedHashMap<>();
//        riskFactors.put("On warfarin", new String[]{"disabled", "major"});
//        riskFactors.put("Older than 65", new String[]{"false", "major"});
//        riskFactors.put("On aspirin", new String[]{"false", "major"});
//        riskFactors.put("Previous gastrointestinal bleed", new String[]{"false", "major"});
//        riskFactors.put("On clopidogrel", new String[]{"false", "major"});
//        riskFactors.put("On Selective Serotonin Reuptake Inhibitor", new String[]{"false"});
//        riskFactors.put("On systemic corticosteroid", new String[]{"true"});
//        model.addObject("riskFactors", riskFactors);

//        model.addObject("risk", 4);

        model.addObject("title", "Colchicine - CYP3A4/PGP inhibitor (DRUGX) interaction");

        Patient patient = r4Service.getPatient("1498");
        return model;
    }


}
