package com.ddinteractjava.cyp3a4;

import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.config.CYP3A4Config;
import com.ddinteractjava.model.Alternative;
import com.ddinteractjava.model.OauthToken;
import com.ddinteractjava.model.RiskFactor;
import com.ddinteractjava.model.Summary;
import com.ddinteractjava.services.CDSService;
import com.ddinteractjava.services.FHIRService;
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
    private String warningSummary = "Consider a dose reduction or alternative: Colchicine has a narrow therapeutic index. Patients who take colchicine and inhibitors of its primary metabolic clearance pathway have a risk of colchicine toxicity.";
    private String dangerSummary = "Avoid combination: Colchicine has a narrow therapeutic index. Patients who take colchicine and inhibitors of its primary metabolic clearance pathway have a risk of colchicine toxicity. This risk is greater in patients with poor renal function.";

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CYP3A4Config cyp3A4Config;

    @Autowired
    private CYP3A4Cache cyp3A4Cache;

    private final Map<String, FHIRService> fhirServiceMap;

    CYP3A4Controller(Map<String, FHIRService> fhirServiceMap) {
        this.fhirServiceMap = fhirServiceMap;
    }

    @Autowired
    SessionCacheService sessionCacheService;

    @Autowired
    CDSService cdsService;

    private List<String> chronicKidneyDiseaseCodes = new ArrayList<>();
    private List<String> akiCodes = new ArrayList<>();
    private List<String> serumCreatinineEGFR = new ArrayList<>();

    // This will serve a cache for patient resource
    // Patient ID to a map of resource type to list of those resources;
    private Map<String, Map<String, List>> patientResources = new HashMap<>();

    @RequestMapping(value = "/cyp3a4", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(name = "state", required = true) String state, HttpServletRequest httpServletRequest) throws IOException {
        ModelAndView model = new ModelAndView("ddinteract");

        OauthToken token = sessionCacheService.getTokenFromCache(state, httpServletRequest.getRemoteAddr());
        String patientId = token.getPatient();
        String clientId = appConfig.getClientId();
        String accessToken = token.getAccessToken();

        getPatientResources(patientId);
        Alternative alternative = suggestAlternatives(patientId);
        model.addObject("alternative", alternative);

        Coding colchicine = findColchicine(patientId);
        model.addObject("colchicine", colchicine);

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

        model.addObject("summary", getSummary(riskFactors, colchicine, alternative));

        //Get images for colchicine and CYP3A4
//        String colchicineImage = rxNormService.getImageUrl(colchicine.getCode());
//        String cyp3a4Image = rxNormService.getImageUrl(alternative.getDrugCode());

        model.addObject("title", "Colchicine - CYP3A4/PGP inhibitor " + alternative.getDrugName() + "  interaction");

        return model;
    }


    private void getPatientResources(String patientId) {
        if (!patientResources.containsKey(patientId)) {
            List<MedicationStatement> medicationStatements = fhirServiceMap.get(appConfig.getFhirVersion()).getMedicationStatements(patientId);
            List<MedicationRequest> medicationRequests = fhirServiceMap.get(appConfig.getFhirVersion()).getMedicationRequest(patientId);
            List<Observation> observations = fhirServiceMap.get(appConfig.getFhirVersion()).getObservations(patientId);
            List<Condition> conditions = fhirServiceMap.get(appConfig.getFhirVersion()).getConditions(patientId);

            Map<String, List> resources = new HashMap<>();
            resources.put("medicationStatement", medicationStatements);
            resources.put("medicationRequest", medicationRequests);
            resources.put("observation", observations);
            resources.put("condition", conditions);
            if (medicationStatements.isEmpty() && medicationRequests.isEmpty() && observations.isEmpty() && conditions.isEmpty()) {
                //Something could have gone wrong when trying to get patient resources, don't want to cache it
            } else {
                patientResources.put(patientId, resources);
            }
        }

    }

    private Coding findColchicine(String patientId) {
        List<MedicationStatement> medicationStatements = patientResources.get(patientId).get("medicationStatement");
        List<MedicationRequest> medicationRequests = patientResources.get(patientId).get("medicationRequest");

        for (MedicationStatement medicationStatement : medicationStatements) {
            List<Coding> coding = ((CodeableConcept) medicationStatement.getMedication()).getCoding();
            for (Coding code : coding) {
                if (cyp3A4Cache.colchicineCodes.contains(code.getCode()))
                    return code;
            }
        }

        for (MedicationRequest medicationRequest : medicationRequests) {
            List<Coding> coding = ((CodeableConcept) medicationRequest.getMedication()).getCoding();
            for (Coding code : coding) {
                if (cyp3A4Cache.colchicineCodes.contains(code.getCode()))
                    return code;
            }
        }
        return null;
    }

    private RiskFactor findAKI(String patientId) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.getAKI());

        List<Condition> conditions = patientResources.get(patientId).get("condition");

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
        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found");
        return riskFactor;
    }

    private RiskFactor findChronicKidneyDisease(String patientId) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.getCKD());

        List<Condition> conditions = patientResources.get(patientId).get("condition");

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

        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found");
        return riskFactor;
    }

    private RiskFactor findSerumCreatinine(String patientId, ModelAndView model) {
        RiskFactor riskFactor = new RiskFactor();
        riskFactor.setRiskName(cyp3A4Config.geteGFR());

        List<Observation> observations = patientResources.get(patientId).get("observation");

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

        riskFactor.setValue(-1);
        model.addObject("serumCreatinineEGFR", -1);
        riskFactor.setHasRiskFactor(false);
        riskFactor.setEffectiveDate("Not found");
        return riskFactor;
    }

    private Alternative findAlternativeCode(Alternative alternative, Coding code) {
        if (cyp3A4Cache.discontinueColchicine.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.DISCONTINUE_COLCHICINE);
            return alternative;
        }
        if (cyp3A4Cache.itraconazoleKetoconazolePosaconazole.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.ITRACONAZOLE_KETOCONAZOLE_POSACONAZOLE);
            return alternative;
        }
        if (cyp3A4Cache.voriconazole.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.VORICONAZOLE);
            return alternative;
        }
        if (cyp3A4Cache.clarithromycin.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.CLARITHROMYCIN);
            return alternative;
        }
        if (cyp3A4Cache.nefazodone.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.NEFAZODONE);
            return alternative;
        }

        if (cyp3A4Cache.erythromycin.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.CLARITHROMYCIN);
            return alternative;
        }
        if (cyp3A4Cache.diltiazemVerapamil.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.DILTIAZEM_VERAPAMIL);
            return alternative;
        }
        if (cyp3A4Cache.dronedarone.contains(code.getCode())) {
            alternative.setDrugName(code.getDisplay());
            alternative.setDrugCode(code.getCode());
            alternative.setAlternativeText(Alternative.DRONEDARONE);
            return alternative;
        }
        return alternative;
    }

    private Alternative suggestAlternatives(String patientId) {
        List<MedicationStatement> medicationStatements = patientResources.get(patientId).get("medicationStatement");
        List<MedicationRequest> medicationRequests = patientResources.get(patientId).get("medicationRequest");

        Alternative alternative = new Alternative();

        for (MedicationStatement medicationStatement : medicationStatements) {
            List<Coding> coding = ((CodeableConcept) medicationStatement.getMedication()).getCoding();
            for (Coding code : coding) {
                findAlternativeCode(alternative, code);
            }
        }

        for (MedicationRequest medicationRequest : medicationRequests) {
            List<Coding> coding = ((CodeableConcept) medicationRequest.getMedication()).getCoding();
            for (Coding code : coding) {
                findAlternativeCode(alternative, code);
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

    private Summary getSummary(List<RiskFactor> riskFactors, Coding colchicine, Alternative alternative) {
        boolean AKI = false;
        boolean CKD = false;
        boolean eGFR = false;

        Summary summary = new Summary();
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
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + alternative.getDrugName().toLowerCase() + "</b>] is one of these strong inhibitors (see the evidence summary below). <b>The risk of a serious adverse event might be reduced</b> by switching to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box). If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
            summary.setWarningSymbol("Warning");
        } else {
            summary.setSummary(dangerSummary);
            summary.setClinicalSummary("Colchicine is a substrate for cytochrome P450 3A4 (CYP3A4) and P-glycoprotein (P-gp). Fatal adverse events have been reported with concomitant use of colchicine with strong CYP3A4 or P-gp inhibitors. The medication [<b>" + alternative.getDrugName().toLowerCase() + "</b>] is one of these strong inhibitors. The risk of a  colchicine toxicity is greater in patients with poor renal function (see the evidence summary below). <b>The safest option would be to switch to a alternative drug that is not a strong inhibitor or stop colchicine (see the \"Alternative Options\" box)</b>. If switching is not an option, monitoring for colchicine toxicity might help avoid a potentially serious adverse event. Symptoms of colchicine toxicity range from mild (e.g., abdominal pain, diarrhea, nausea, vomiting) to moderate (e.g., muscle pain, muscle weakness) to fatal (e.g., cardiac failure, renal failure). A strong monitoring strategy would include advising the patient to monitor for these symptoms.");
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
