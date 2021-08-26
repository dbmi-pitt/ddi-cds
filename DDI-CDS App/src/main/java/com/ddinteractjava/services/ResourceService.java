package com.ddinteractjava.services;

import com.ddinteractjava.config.AppConfig;
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
        } catch (Exception e ) {
            return "";
        }
        return "";
    }
}
