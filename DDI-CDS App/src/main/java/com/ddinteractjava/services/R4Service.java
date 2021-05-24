package com.ddinteractjava.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.ddinteractjava.config.AppConfig;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class R4Service {
    public IGenericClient client;

    @Autowired
    private AppConfig appConfig;

    @PostConstruct
    public void setupClient() {
        FhirContext fhirContext = FhirContext.forR4();
        fhirContext.getRestfulClientFactory().setConnectTimeout(20 * 1000);
        fhirContext.getRestfulClientFactory().setSocketTimeout(20 * 1000);

        client = fhirContext.newRestfulGenericClient(appConfig.getFhirUrl());
        client.setEncoding(EncodingEnum.JSON);
        client.setPrettyPrint(true);

         if (appConfig.getStaticToken() != null) {
            addBearerToken(appConfig.getStaticToken());
        }
    }

    public void addBearerToken(String token) {
        //Need to remove previous access token and replace with new one
        client.getInterceptorService().unregisterAllInterceptors();
        IClientInterceptor authInterceptor = new BearerTokenAuthInterceptor(token);
        client.registerInterceptor(authInterceptor);
    }

    public Patient getPatient(String patientId) {
        return client.read().resource(Patient.class).withId(patientId).execute();
    }

    public String getPrefetch(String patientId) {
        String searchUrl = appConfig.getFhirUrl() + "/Patient/" + patientId + "/$everything";
        Bundle response = client.search().byUrl(searchUrl).encodedJson().returnBundle(Bundle.class).execute();
        FhirContext fhirContext = FhirContext.forR4();
        IParser parser = fhirContext.newJsonParser();
        String serialized = parser.encodeResourceToString(response);

        return serialized;
    }

    public List<MedicationStatement> getMedicationStatements(String patientId) {
        List<MedicationStatement> medicationStatements = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/MedicationStatement?patient=" + patientId;
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.BundleEntryComponent entryComponent : response.getEntry()) {
                medicationStatements.add((MedicationStatement) entryComponent.getResource());
            }
        } catch (InvalidRequestException e) {
            //No medicationstatement for this patient was found
        }
        return medicationStatements;
    }

    public List<MedicationRequest> getMedicationRequest(String patientId) {
        List<MedicationRequest> medicationRequests = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/MedicationRequest?patient=" + patientId;
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.BundleEntryComponent entryComponent : response.getEntry()) {
                medicationRequests.add((MedicationRequest) entryComponent.getResource());
            }
        } catch (InvalidRequestException e) {
            //No medicationrequest for this patient was found
        }
        return medicationRequests;
    }

    public List<Observation> getObservations(String patientId) {
        List<Observation> observations = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/Observation?patient=" + patientId;
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.BundleEntryComponent entryComponent : response.getEntry()) {
                observations.add((Observation) entryComponent.getResource());
            }
        } catch (InvalidRequestException e) {
            //No observation for this patient was found
        }
        return observations;
    }

    public List<Condition> getConditions(String patientId) {
        List<Condition> conditions = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/Condition?patient=" + patientId;
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.BundleEntryComponent entryComponent : response.getEntry()) {
                conditions.add((Condition) entryComponent.getResource());
            }
        } catch (InvalidRequestException e) {
            //No condition for this patient was found
        }
        return conditions;
    }
}
