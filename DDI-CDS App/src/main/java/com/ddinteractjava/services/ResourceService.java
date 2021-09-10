package com.ddinteractjava.services;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import com.ddinteractjava.config.AppConfig;
import com.ddinteractjava.model.SimpleCode;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {
    @Autowired
    private AppConfig appConfig;

    private final Map<String, FHIRService> fhirServiceMap;

    ResourceService(Map<String, FHIRService> fhirServiceMap) {
        this.fhirServiceMap = fhirServiceMap;
    }

    // This will serve a cache for patient resource
    // Patient ID to a map of resource type to list of those resources;
    public Map<String, Map<String, List>> patientResources = new HashMap<>();

    public void getPatientResources(String patientId) {
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

            patientResources.put(patientId, resources);
        }
    }

    public String getPatientName(String patientId) {
        try {
            if (appConfig.getFhirVersion().equals("r4")) {
                return ((Patient) fhirServiceMap.get(appConfig.getFhirVersion()).getPatient(patientId)).getName().get(0).getGiven().get(0).toString();
            } else if (appConfig.getFhirVersion().equals("stu2")) {
                return ((ca.uhn.fhir.model.dstu2.resource.Patient) fhirServiceMap.get(appConfig.getFhirVersion()).getPatient(patientId)).getName().get(0).getGiven().get(0).toString();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public SimpleCode findPrimaryDrug(String patientId, List<String> primaryDrugList, String drugType) {
        SimpleCode simpleCode = new SimpleCode();
        simpleCode.setDrugType(drugType);

        if (appConfig.getFhirVersion().equals("r4")) {
            List<MedicationStatement> medicationStatements = patientResources.get(patientId).get("medicationStatement");
            List<MedicationRequest> medicationRequests = patientResources.get(patientId).get("medicationRequest");

            for (MedicationStatement medicationStatement : medicationStatements) {
                List<Coding> coding = ((CodeableConcept) medicationStatement.getMedication()).getCoding();
                for (Coding code : coding) {
                    if (primaryDrugList.contains(code.getCode())) {
                        simpleCode.setCode(code.getCode());
                        simpleCode.setDisplay(code.getDisplay());
                        simpleCode.setDrugType(drugType);
                    }
                }
            }

            for (MedicationRequest medicationRequest : medicationRequests) {
                List<Coding> coding = ((CodeableConcept) medicationRequest.getMedication()).getCoding();
                for (Coding code : coding) {
                    if (primaryDrugList.contains(code.getCode())) {
                        simpleCode.setCode(code.getCode());
                        simpleCode.setDisplay(code.getDisplay());
                    }
                }
            }
        } else if (appConfig.getFhirVersion().equals("stu2")) {
            List<ca.uhn.fhir.model.dstu2.resource.MedicationStatement> medicationStatements = patientResources.get(patientId).get("medicationStatement");
            List<MedicationOrder> medicationRequests = patientResources.get(patientId).get("medicationRequest");

            for (ca.uhn.fhir.model.dstu2.resource.MedicationStatement medicationStatement : medicationStatements) {
                List<CodingDt> coding = ((CodeableConceptDt) medicationStatement.getMedication()).getCoding();
                for (CodingDt code : coding) {
                    if (primaryDrugList.contains(code.getCode())) {
                        simpleCode.setCode(code.getCode());
                        simpleCode.setDisplay(code.getDisplay());
                    }
                }
            }

            for (MedicationOrder medicationRequest : medicationRequests) {
                List<CodingDt> coding = ((CodeableConceptDt) medicationRequest.getMedication()).getCoding();
                for (CodingDt code : coding) {
                    if (primaryDrugList.contains(code.getCode())) {
                        simpleCode.setCode(code.getCode());
                        simpleCode.setDisplay(code.getDisplay());
                    }
                }
            }
        }
        return simpleCode;
    }
}
