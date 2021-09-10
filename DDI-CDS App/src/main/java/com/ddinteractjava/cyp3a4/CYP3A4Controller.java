package com.ddinteractjava.cyp3a4;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.config.CYP3A4Config;
import com.ddinteractjava.model.*;
import com.ddinteractjava.services.CDSService;
import com.ddinteractjava.services.ResourceService;
import com.ddinteractjava.services.SessionCacheService;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class CYP3A4Controller {
    private String pattern = "MM/dd/yyyy";
    private String warningSummary = "Consider a dose reduction or alternative: <ul>" +
            "<li>Colchicine has a narrow therapeutic index.</li>" +
            "<li>Patients who take colchicine and inhibitors of its primary metabolic clearance pathway have a risk of colchicine toxicity.</li>" +
            "</ul>";
    private String dangerSummary = "Avoid combination: <ul>" +
            "<li>Colchicine has a narrow therapeutic index.</li> " +
            "<li>Patients who take colchicine and inhibitors of its primary metabolic clearance pathway have a risk of colchicine toxicity.</li>" +
            "<li>This risk is greater in patients with poor renal function.</li>" +
            "</ul>";

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private CYP3A4Config cyp3A4Config;

    @Autowired
    private CYP3A4Cache cyp3A4Cache;

    @Autowired
    SessionCacheService sessionCacheService;

    @Autowired
    CDSService cdsService;

    private List<String> chronicKidneyDiseaseCodes = new ArrayList<>();
    private List<String> akiCodes = new ArrayList<>();
    private List<String> serumCreatinineEGFR = new ArrayList<>();

    @RequestMapping(value = "/colchicine", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(name = "state", required = true) String state, HttpServletRequest httpServletRequest) throws IOException {
        ModelAndView model = new ModelAndView("ddinteract");

        OauthToken token = sessionCacheService.getTokenFromCache(state, httpServletRequest.getRemoteAddr());
        String patientId = token.getPatient();
        String clientId = appConfig.getClientId();
        String accessToken = token.getAccessToken();

        resourceService.getPatientResources(patientId);
        model.addObject("patientName", resourceService.getPatientName(patientId));

        Alternative alternative = suggestAlternatives(patientId);
        model.addObject("alternative", alternative);

        //Find Colchicine
        SimpleCode primaryDrug = resourceService.findPrimaryDrug(patientId, cyp3A4Cache.colchicineCodes, "colchicine");
        model.addObject("primaryDrug", primaryDrug);

//        String card = cdsService.callPatientView(accessToken, clientId, patientId);
//        model.addObject("card", card);

        Map<String, String> patientProfiles = new LinkedHashMap<>();
        patientProfiles.put("/launchStandalone?patient=99995", "Patient on Itraconazole with AKI and Chronic Kidney Disease");
        patientProfiles.put("/launchStandalone?patient=99996", "Patient on Ritonavir with AKI and Chronic Kidney Disease");
        model.addObject("patientProfiles", patientProfiles);

        List<RiskFactor> riskFactors = new LinkedList<>();
        riskFactors.add(findAKI(patientId));
        riskFactors.add(findChronicKidneyDisease(patientId));
        riskFactors.add(findSerumCreatinine(patientId, model));
        model.addObject("riskFactors", riskFactors);

        model.addObject("summary", getSummary(riskFactors, primaryDrug, alternative));

        //Get images for colchicine and CYP3A4
//        String colchicineImage = rxNormService.getImageUrl(colchicine.getCode());
//        String cyp3a4Image = rxNormService.getImageUrl(alternative.getDrugCode());

        model.addObject("title", "Interaction between Colchicine and CYP3A4/PGP inhibitor [" + alternative.getDrugName() + "]");

        return model;
    }

    private RiskFactor findAKI(String patientId) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.getAKI());

        if (appConfig.getFhirVersion().equals("r4")) {
            List<Condition> conditions = resourceService.patientResources.get(patientId).get("condition");

            for (Condition condition : conditions) {
                List<Coding> coding = condition.getCode().getCoding();
                for (Coding code : coding) {
                    if (akiCodes.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
                        if (condition.hasOnsetDateTimeType()) {
                            Date onsetDate = ((DateTimeType) condition.getOnset()).getValue();
                            DateFormat df = new SimpleDateFormat(pattern);

                            riskFactor.setResourceName(code.getDisplay());
                            riskFactor.setEffectiveDate("Diagnosis as of " + df.format(onsetDate));
                            return riskFactor;
                        }
                    }
                }
            }
        } else if (appConfig.getFhirVersion().equals("stu2")) {
            List<ca.uhn.fhir.model.dstu2.resource.Condition> conditions = resourceService.patientResources.get(patientId).get("condition");

            for (ca.uhn.fhir.model.dstu2.resource.Condition condition : conditions) {
                List<CodingDt> coding = condition.getCode().getCoding();
                for (CodingDt code : coding) {
                    if (akiCodes.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
                        if (!condition.getOnset().isEmpty()) {
                            Date onsetDate = ((DateTimeType) condition.getOnset()).getValue();
                            DateFormat df = new SimpleDateFormat(pattern);

                            riskFactor.setResourceName(code.getDisplay());
                            riskFactor.setEffectiveDate("Diagnosis as of " + df.format(onsetDate));
                            return riskFactor;
                        }
                    }
                }
            }
        }
        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found in EHR");
        return riskFactor;
    }

    private RiskFactor findChronicKidneyDisease(String patientId) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.getCKD());
        if (appConfig.getFhirVersion().equals("r4")) {
            List<Condition> conditions = resourceService.patientResources.get(patientId).get("condition");

            for (Condition condition : conditions) {
                List<Coding> coding = condition.getCode().getCoding();
                for (Coding code : coding) {
                    if (chronicKidneyDiseaseCodes.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
                        if (condition.hasOnsetDateTimeType()) {
                            Date onsetDate = ((DateTimeType) condition.getOnset()).getValue();
                            DateFormat df = new SimpleDateFormat(pattern);
                            riskFactor.setResourceName(code.getDisplay());
                            riskFactor.setEffectiveDate("Diagnosis as of " + df.format(onsetDate));
                            return riskFactor;
                        }
                    }
                }
            }
        } else if (appConfig.getFhirVersion().equals("stu2")) {
            List<ca.uhn.fhir.model.dstu2.resource.Condition> conditions = resourceService.patientResources.get(patientId).get("condition");

            for (ca.uhn.fhir.model.dstu2.resource.Condition condition : conditions) {
                List<CodingDt> coding = condition.getCode().getCoding();
                for (CodingDt code : coding) {
                    if (chronicKidneyDiseaseCodes.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
                        if (!condition.getOnset().isEmpty()) {
                            Date onsetDate = ((DateTimeType) condition.getOnset()).getValue();
                            DateFormat df = new SimpleDateFormat(pattern);

                            riskFactor.setResourceName(code.getDisplay());
                            riskFactor.setEffectiveDate("Diagnosis as of " + df.format(onsetDate));
                            return riskFactor;
                        }
                    }
                }
            }
        }

        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found in EHR");
        return riskFactor;
    }

    private RiskFactor findSerumCreatinine(String patientId, ModelAndView model) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.geteGFR());
        if (appConfig.getFhirVersion().equals("r4")) {

            List<Observation> observations = resourceService.patientResources.get(patientId).get("observation");

            for (Observation observation : observations) {
                List<Coding> coding = observation.getCode().getCoding();
                for (Coding code : coding) {
                    if (serumCreatinineEGFR.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
                        if (observation.hasEffectiveDateTimeType() && observation.hasValueQuantity()) {
                            Date effectiveDate = ((DateTimeType) observation.getEffective()).getValue();
                            DateFormat df = new SimpleDateFormat(pattern);
                            riskFactor.setResourceName(code.getDisplay());
                            riskFactor.setEffectiveDate("Measurement of " + observation.getValueQuantity().getValue().doubleValue() + " " + observation.getValueQuantity().getUnit() + " as of " + df.format(effectiveDate));
                            riskFactor.setValue(observation.getValueQuantity().getValue().doubleValue());
                            model.addObject("serumCreatinineEGFR", riskFactor.getValue());
                            return riskFactor;
                        }
                    }
                }
            }
        } else if (appConfig.getFhirVersion().equals("stu2")) {
            List<ca.uhn.fhir.model.dstu2.resource.Observation> observations = resourceService.patientResources.get(patientId).get("observation");

            for (ca.uhn.fhir.model.dstu2.resource.Observation observation : observations) {
                List<CodingDt> coding = observation.getCode().getCoding();
                for (CodingDt code : coding) {
                    if (serumCreatinineEGFR.contains(code.getCode())) {
                        riskFactor.setHasRiskFactor(true);
//                        if (!observation.getEffective().isEmpty() && observation.hasValueQuantity()) {
//                            Date effectiveDate = ((DateTimeType) observation.getEffective()).getValue();
//                            DateFormat df = new SimpleDateFormat(pattern);
//                            riskFactor.setResourceName(code.getDisplay());
//                            riskFactor.setEffectiveDate("Measurement of " + observation.getValueQuantity().getValue().doubleValue() + " " + observation.getValueQuantity().getUnit() + " as of " + df.format(effectiveDate));
//                            riskFactor.setValue(observation.getValueQuantity().getValue().doubleValue());
//                            model.addObject("serumCreatinineEGFR", riskFactor.getValue());
//                            return riskFactor;
//                        }
                    }
                }
            }
        }

        riskFactor.setValue(-1);
        model.addObject("serumCreatinineEGFR", -1);
        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found in EHR");
        return riskFactor;
    }

    private Alternative findAlternativeCode(Alternative alternative, String code, String display) {
        boolean foundAlt = false;
        if (cyp3A4Cache.discontinueColchicine.contains(code)) {
            alternative.setAlternativeText(Alternative.DISCONTINUE_COLCHICINE);
            foundAlt = true;
        } else if (cyp3A4Cache.itraconazoleKetoconazolePosaconazole.contains(code)) {
            alternative.setAlternativeText(Alternative.ITRACONAZOLE_KETOCONAZOLE_POSACONAZOLE);
            foundAlt = true;
        } else if (cyp3A4Cache.voriconazole.contains(code)) {
            alternative.setAlternativeText(Alternative.VORICONAZOLE);
            foundAlt = true;
        } else if (cyp3A4Cache.clarithromycin.contains(code)) {
            alternative.setAlternativeText(Alternative.CLARITHROMYCIN);
            foundAlt = true;
        } else if (cyp3A4Cache.nefazodone.contains(code)) {
            alternative.setAlternativeText(Alternative.NEFAZODONE);
            foundAlt = true;
        } else if (cyp3A4Cache.erythromycin.contains(code)) {
            alternative.setAlternativeText(Alternative.CLARITHROMYCIN);
            foundAlt = true;
        } else if (cyp3A4Cache.diltiazemVerapamil.contains(code)) {
            alternative.setAlternativeText(Alternative.DILTIAZEM_VERAPAMIL);
            foundAlt = true;
        } else if (cyp3A4Cache.dronedarone.contains(code)) {
            alternative.setAlternativeText(Alternative.DRONEDARONE);
            foundAlt = true;
        }

        if(foundAlt) {
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

    @RequestMapping(value = "/cyp3a4Summary", method = RequestMethod.GET)
    @ResponseBody
    public String getSummary(String[] checkedRiskFactors, String[] uncheckedRiskFactors, Double eGFRValue, String alternativeDrug) {
        boolean hasAKI = false;
        boolean hasCKD = false;
        boolean eGFR = false;
//        JsonObject json = new JsonObject();
        Summary summary = new Summary();

        if (checkedRiskFactors != null) {
            for (String riskFactor : checkedRiskFactors) {
                if (riskFactor.equals(cyp3A4Config.getAKI()))
                    hasAKI = true;
                if (riskFactor.equals(cyp3A4Config.getCKD()))
                    hasCKD = true;
                if (riskFactor.equals(cyp3A4Config.geteGFR())) {
                    if (eGFRValue != null && (eGFRValue < 60 && eGFRValue >= 0))
                        eGFR = true;
                }
            }
        }

        if (!hasAKI && !hasCKD && !eGFR) {
            summary.setSummary(warningSummary);
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + alternativeDrug.toLowerCase() + "</b>] is one of these strong inhibitors (see the evidence summary below). <b>The risk of a serious adverse event might be reduced</b> by switching to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box). If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
            summary.setWarningSymbol("<h2><span class=\"badge badge-warning\">Warning</span></h2>");
            return appConfig.gson().toJson(summary);

        } else {
            summary.setSummary(dangerSummary);
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + alternativeDrug.toLowerCase() + "</b>] is one of these strong inhibitors. The risk of a  colchicine toxicity is greater in patients with poor renal function (see the evidence summary below). <b>The safest option would be to switch to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box)</b>. If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
            summary.setWarningSymbol("<h2><span class=\"badge badge-danger\">Danger</span></h2>");
            return appConfig.gson().toJson(summary);
        }
    }

    private Summary getSummary(List<RiskFactor> riskFactors, SimpleCode colchicine, Alternative alternative) {
        boolean AKI = false;
        boolean CKD = false;
        boolean eGFR = false;

        Summary summary = new Summary();
        String clinicalSummaryDrug = "";
        if (alternative.getDrugName() != null && !alternative.getDrugName().equals("")) {
            clinicalSummaryDrug = alternative.getDrugName().toLowerCase();
        }
        if (alternative.getDrugCode() == null && colchicine == null) {
            summary.setSummary("Patient is not currently on a Colchicine or a CYP3A4/PGP inhibitor");
            summary.setClinicalSummary("Patient is not currently on a Colchicine or a CYP3A4/PGP inhibitor");
            return summary;
        }
        for (RiskFactor riskFactor : riskFactors) {
            if (riskFactor.getRiskName().equals(cyp3A4Config.getAKI())) {
                if (riskFactor.getHasRiskFactor() == true)
                    AKI = true;
            }
            if (riskFactor.getRiskName().equals(cyp3A4Config.getCKD())) {
                if (riskFactor.getHasRiskFactor() == true)
                    CKD = true;
            }
            if (riskFactor.getRiskName().equals(cyp3A4Config.geteGFR())) {
                if (riskFactor.getHasRiskFactor() == true && riskFactor.getValue() < 60 && riskFactor.getValue() >= 0)
                    eGFR = true;
            }
        }

        if (!AKI && !CKD && !eGFR) {
            summary.setSummary(warningSummary);
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + clinicalSummaryDrug + "</b>] is one of these strong inhibitors (see the evidence summary below). <b>The risk of a serious adverse event might be reduced</b> by switching to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box). If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
            summary.setWarningSymbol("Warning");
        } else {
            summary.setSummary(dangerSummary);
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + clinicalSummaryDrug + "</b>] is one of these strong inhibitors. The risk of a  colchicine toxicity is greater in patients with poor renal function (see the evidence summary below). <b>The safest option would be to switch to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box)</b>. If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
            summary.setWarningSymbol("Danger");
        }

        return summary;
    }

    @PostConstruct
    private void loadFiles() {
        if (chronicKidneyDiseaseCodes.isEmpty()) {
            ClassPathResource res = new ClassPathResource("cyp3a4/chronic-kidney-disease.txt");
            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
                chronicKidneyDiseaseCodes = lines.collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (akiCodes.isEmpty()) {
            ClassPathResource res = new ClassPathResource("cyp3a4/AKI.txt");
            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
                akiCodes = lines.collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serumCreatinineEGFR.isEmpty()) {
            ClassPathResource res = new ClassPathResource("cyp3a4/serumCreatinineEGFR.txt");
            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
                serumCreatinineEGFR = lines.collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (colchicine.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/colchicine.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                colchicine = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (itraconazoleKetoconazolePosaconazole.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/itraconazole-ketoconazole-posaconazole.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                itraconazoleKetoconazolePosaconazole = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (voriconazole.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/voriconazole.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                voriconazole = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (clarithromycin.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/clarithromycin.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                clarithromycin = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (nefazodone.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/nefazodone.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                nefazodone = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (diltiazemVerapamil.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/diltiazem-verapamil.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                diltiazemVerapamil = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (dronedarone.isEmpty()) {
//            ClassPathResource res = new ClassPathResource("cyp3a4/dronedarone.txt");
//            try (Stream<String> lines = Files.lines(res.getFile().toPath())) {
//                dronedarone = lines.collect(Collectors.toList());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

}
