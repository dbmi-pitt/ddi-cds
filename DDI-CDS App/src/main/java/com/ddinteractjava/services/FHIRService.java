package com.ddinteractjava.services;


import ca.uhn.fhir.rest.client.api.IGenericClient;

import java.util.List;

public interface FHIRService<T> {
    IGenericClient getClient();

    void addBearerToken(String token);

    T getPatient(String patientId);

    List<T> getMedicationStatements(String patientId);

    List<T> getMedicationRequest(String patientId);

    List<T> getObservations(String patientId);

    List<T> getConditions(String patientId);

    String getPrefetch(String patientId);
}
