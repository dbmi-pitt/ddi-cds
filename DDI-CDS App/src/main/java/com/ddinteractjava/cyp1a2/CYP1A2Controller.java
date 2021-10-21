package com.ddinteractjava.cyp1a2;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.config.CYP1A2Config;
import com.ddinteractjava.model.*;
import com.ddinteractjava.services.ResourceService;
import com.ddinteractjava.services.SessionCacheService;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CYP1A2Controller {

    private String warningSummary = "Consider Alternative: <ul>" +
            "<li>CYP1A2 inhibitor medications can increase tizanidine serum concentrations by more than 5-fold, leading to higher rates of tizanidine adverse events: decrease in blood pressure, heart rate and occasionally fatalities.</li>" +
            "</ul>";

    private String zafirlukastSummary = "Use Alternative: <ul>" +
            "<li>Concurrent use of tizanidine and zafirlukast could lead to tizanidine-related adverse events such as decrease in blood pressure, trate, fatigue, and somnolence.</li>" +
            "</ul>";
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private CYP1A2Config cyp1a2Config;

    @Autowired
    private CYP1A2Cache cyp1A2Cache;

    @Autowired
    SessionCacheService sessionCacheService;

    @RequestMapping(value = "/tizanidine", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(name = "state", required = true) String state, HttpServletRequest httpServletRequest) throws IOException {
        ModelAndView model = new ModelAndView("ddinteract");

        OauthToken token = sessionCacheService.getTokenFromCache(state, httpServletRequest.getRemoteAddr());
        String patientId = token.getPatient();
        resourceService.getPatientResources(patientId);

        resourceService.getPatientResources(patientId);
        model.addObject("patientName", resourceService.getPatientName(patientId));

        Alternative alternative = suggestAlternatives(patientId);
        model.addObject("alternative", alternative);

        //Find Tizanidine
        SimpleCode primaryDrug = resourceService.findPrimaryDrug(patientId, cyp1A2Cache.tizanidineCodes, "tizanidine");
        model.addObject("primaryDrug", primaryDrug);

        model.addObject("summary", getSummary(primaryDrug, alternative));

        model.addObject("title", "Interaction between Tizanidine and CYP1A2 inhibitor [" + alternative.getDrugName() + "]");

        Map<String, String> patientProfiles = new LinkedHashMap<>();
        patientProfiles.put("/launchStandalone?ddi=tizanidine&patient=100001", "Patient on Ciprofloxacin");
        patientProfiles.put("/launchStandalone?ddi=tizanidine&patient=100002", "Patient on Zafirlukast");
        patientProfiles.put("/launchStandalone?ddi=tizanidine&patient=100003", "Patient on Fluvoxamine");
        patientProfiles.put("/launchStandalone?ddi=tizanidine&patient=100004", "Patient on Phenylpropanolamine");
        model.addObject("patientProfiles", patientProfiles);

        return model;
    }

    private Alternative findAlternativeCode(Alternative alternative, String code, String display) {
        boolean foundAlt = false;
        if (cyp1A2Cache.ciprofloxacinAlternatives.contains(code)) {
            alternative.setAlternativeText(Alternative.CIPROFLOXACIN_ALTERNATIVE);
            foundAlt = true;
        } else if (cyp1A2Cache.fluvoxamineAlternatives.contains(code)) {
            alternative.setAlternativeText(Alternative.FLUVOXAMINE_ALTERNATIVE);
            foundAlt = true;
        } else if (cyp1A2Cache.zafirlukastAlternatives.contains(code)) {
            alternative.setAlternativeText(Alternative.ZAFIRLUKAST_ALTERNATIVES);
            foundAlt = true;
        } else if (cyp1A2Cache.cyp1a2Codes.contains(code)) {
            alternative.setAlternativeText("Alternatives to avoid a tizanidine-CYP1A2 interaction include medications not inhibiting the CYP1A2 such as: (antibiotics: levofloxacin, penicillins), (SSRI antidepressants: fluoxetine, sertraline, and others); leukotriene receptor antagonists (montelukast).");
            foundAlt = true;
        }

        if (foundAlt) {
            alternative.setDrugName(display);
            alternative.setDrugCode(code);
        }

        return alternative;
    }

    private Alternative suggestAlternatives(String patientId) {
        Alternative alternative = new Alternative();
        if (appConfig.getFhirVersion().equals("r4")) {
            List<MedicationStatement> medicationStatements = resourceService.patientResources.get(patientId).get("medicationStatement");
            List<MedicationRequest> medicationRequests = resourceService.patientResources.get(patientId).get("medicationRequest");

            for (MedicationStatement medicationStatement : medicationStatements) {
                List<Coding> coding = ((CodeableConcept) medicationStatement.getMedication()).getCoding();
                for (Coding code : coding) {
                    findAlternativeCode(alternative, code.getCode(), code.getDisplay());
                }
            }

            for (MedicationRequest medicationRequest : medicationRequests) {
                List<Coding> coding = ((CodeableConcept) medicationRequest.getMedication()).getCoding();
                for (Coding code : coding) {
                    findAlternativeCode(alternative, code.getCode(), code.getDisplay());
                }
            }
        } else if (appConfig.getFhirVersion().equals("stu2")) {
            List<ca.uhn.fhir.model.dstu2.resource.MedicationStatement> medicationStatements = resourceService.patientResources.get(patientId).get("medicationStatement");
            List<MedicationOrder> medicationRequests = resourceService.patientResources.get(patientId).get("medicationRequest");

            for (ca.uhn.fhir.model.dstu2.resource.MedicationStatement medicationStatement : medicationStatements) {
                List<CodingDt> coding = ((CodeableConceptDt) medicationStatement.getMedication()).getCoding();
                for (CodingDt code : coding) {
                    findAlternativeCode(alternative, code.getCode(), code.getDisplay());
                }
            }

            for (MedicationOrder medicationOrder : medicationRequests) {
                List<CodingDt> coding = ((CodeableConceptDt) medicationOrder.getMedication()).getCoding();
                for (CodingDt code : coding) {
                    findAlternativeCode(alternative, code.getCode(), code.getDisplay());
                }
            }
        }

        return alternative;
    }

    private Summary getSummary(SimpleCode tizanidine, Alternative alternative) {
        Summary summary = new Summary();
        String clinicalSummaryDrug = "";
        if (alternative.getDrugName() != null && !alternative.getDrugName().equals("")) {
            clinicalSummaryDrug = alternative.getDrugName().toLowerCase();
        }
        if (alternative.getDrugCode() == null && tizanidine.getCode() == null) {
            summary.setSummary("Patient is not currently on a Tizanidine or a CYP1A2 inhibitor");
            summary.setClinicalSummary("Patient is not currently on a Tizanidine or a CYP1A2 inhibitor");
            return summary;
        }

        //Set the warning symbol and clinical summary for the 3 primary use cases, they will be over-written in else case
        summary.setClinicalSummary("Tizanidine has a narrow therapeutic range and a low oral bioavailability due to the extensive first-pass metabolism via cytochrome P450 (CYP 1A2). Concurrent use of tizanidine and strong CYP1A2 inhibitors, such as " + clinicalSummaryDrug + ", is not recommended because it may significantly increase tizanidine levels which can lead to severe adverse events. " +
                "<br></br>Tizanidine is a substrate for cytochrome P450 1A2 (CYP1A2). [<b>" + clinicalSummaryDrug + "</b>] is a potent CYP1A2 inhibitor. Preferred management options are to use an alternative medication that is not a strong CYP1A2 inhibitor or to discontinue the tizanidine (see the <b>\"Alternative Options\"</b> box)</b>. It is also recommended to advise the patient to monitor for signs of hypotension, including: dizziness, fatigue, weakness, and somnolence.");
        summary.setWarningSymbol("Danger");

        if (alternative.getAlternativeText().equals(Alternative.CIPROFLOXACIN_ALTERNATIVE) ||
                alternative.getAlternativeText().equals(Alternative.FLUVOXAMINE_ALTERNATIVE)) {
            summary.setSummary("Use Alternative: <ul>" +
                    "<li>Increased tizanidine serum levels can lead to severe adverse events (substantial decreases in blood pressure, heart rate, dizziness, fall, etc.).</li>" +
                    "<li>Patients who take tizanidine and a drug inhibiting its primary metabolic clearance pathway such as " + clinicalSummaryDrug + " have higher risk of tizanidine adverse events.</li>" +
                    "</ul>");
        } else if (alternative.getAlternativeText().equals(Alternative.ZAFIRLUKAST_ALTERNATIVES)) {
            summary.setSummary(zafirlukastSummary);
        } else {
            summary.setSummary(warningSummary);
            summary.setClinicalSummary("Tizanidine has a narrow therapeutic range and a low oral bioavailability due to the extensive first-pass metabolism via cytochrome P450 (CYP 1A2). Concurrent use of tizanidine and strong CYP1A2 inhibitors is not recommended because it may significantly increase tizanidine levels which can lead to severe adverse events.");
            summary.setWarningSymbol("Warning");
        }

        return summary;
    }


}
