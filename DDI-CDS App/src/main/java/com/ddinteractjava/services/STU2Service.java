package com.ddinteractjava.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.ddinteractjava.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class STU2Service implements FHIRService {
    public IGenericClient client;

    @Autowired
    private AppConfig appConfig;

    @PostConstruct
    public void setupClient() {
        FhirContext fhirContext = FhirContext.forDstu2();
        fhirContext.getRestfulClientFactory().setConnectTimeout(20 * 1000);
        fhirContext.getRestfulClientFactory().setSocketTimeout(20 * 1000);

        client = fhirContext.newRestfulGenericClient(appConfig.getFhirUrl());
        client.setEncoding(EncodingEnum.JSON);
        client.setPrettyPrint(true);

        if (appConfig.getStaticToken() != null) {
            addBearerToken(appConfig.getStaticToken());
        }
    }

    public IGenericClient getClient() {
        return client;
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
        FhirContext fhirContext = FhirContext.forDstu2();
        IParser parser = fhirContext.newJsonParser();
        String serialized = parser.encodeResourceToString(response);

        return serialized;
    }

    public List<MedicationStatement> getMedicationStatements(String patientId) {
        List<MedicationStatement> medicationStatements = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/MedicationStatement?patient=" + patientId;
            System.out.println("Medication Statement search url: " + searchUrl);
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.Entry entryComponent : response.getEntry()) {

                System.out.println("Response: "  + entryComponent.getResponse().toString());
                System.out.println("Resource: "  + entryComponent.getResource().toString());

                medicationStatements.add((MedicationStatement) entryComponent.getResource());
            }
        } catch (ResourceNotFoundException e) {
            //No medicationStatements for this patient was found
        } catch (InvalidRequestException e) {
            //No medicationStatements for this patient was found
        }
        return medicationStatements;
    }

    public List<MedicationOrder> getMedicationRequest(String patientId) {
        List<MedicationOrder> medicationRequests = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/MedicationRequest?patient=" + patientId;
            System.out.println("Medication Request search url: " + searchUrl);
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.Entry entryComponent : response.getEntry()) {
                medicationRequests.add((MedicationOrder) entryComponent.getResource());
            }
        } catch (ResourceNotFoundException e) {
            //No medicationRequests for this patient was found
        } catch (InvalidRequestException e) {
            //No medicationRequests for this patient was found
        }
        return medicationRequests;
    }

    public List<Observation> getObservations(String patientId) {
        List<Observation> observations = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/Observation?patient=" + patientId;
            System.out.println("Observation search url: " + searchUrl);
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.Entry entryComponent : response.getEntry()) {
                observations.add((Observation) entryComponent.getResource());
            }
        } catch (ResourceNotFoundException e) {
            //No observation for this patient was found
        } catch (InvalidRequestException e) {
            //No observation for this patient was found
        }
        return observations;
    }

    public List<Condition> getConditions(String patientId) {
        List<Condition> conditions = new ArrayList<>();
        try {
            String searchUrl = appConfig.getFhirUrl() + "/Condition?patient=" + patientId;
            System.out.println("Condition search url: " + searchUrl);
            Bundle response = client.search().byUrl(searchUrl).returnBundle(Bundle.class).execute();
            for (Bundle.Entry entryComponent : response.getEntry()) {
                conditions.add((Condition) entryComponent.getResource());
            }
        } catch (ResourceNotFoundException e) {
            //No condition for this patient was found
        } catch (InvalidRequestException e) {
            //No condition for this patient was found
        }
        return conditions;
    }
}
