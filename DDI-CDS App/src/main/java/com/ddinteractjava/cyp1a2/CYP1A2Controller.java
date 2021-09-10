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
import java.util.List;

@Controller
public class CYP1A2Controller {

    private String warningSummary = "Consider a dose reduction or alternative: <ul>" +
            "<li>Colchicine has a narrow therapeutic index.</li>" +
            "<li>Patients who take colchicine and inhibitors of its primary metabolic clearance pathway have a risk of colchicine toxicity.</li>" +
            "</ul>";
    private String dangerSummary = "Use Alternative: <ul>" +
            "<li>Concurrent use of tizanidine and ciprofloxacin has been reported to induce adverse effects of tizanidine such as lowering blood pressure (BP), and heart rate (HR)</li> " +

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
        if (alternative.getDrugCode() == null && tizanidine == null) {
            summary.setSummary("Patient is not currently on a Tizanidine or a CYP1A2 inhibitor");
            summary.setClinicalSummary("Patient is not currently on a Tizanidine or a CYP1A2 inhibitor");
            return summary;
        }

        if (alternative.getAlternativeText().equals(Alternative.CIPROFLOXACIN_ALTERNATIVE) ||
                alternative.getAlternativeText().equals(Alternative.FLUVOXAMINE_ALTERNATIVE) ||
                alternative.getAlternativeText().equals(Alternative.ZAFIRLUKAST_ALTERNATIVES)) {
            summary.setSummary(dangerSummary);
            summary.setClinicalSummary("");
            summary.setWarningSymbol("Danger");
        } else {
            summary.setSummary(warningSummary);
            summary.setClinicalSummary("");
            summary.setWarningSymbol("Warning");
        }

        return summary;
    }


}
