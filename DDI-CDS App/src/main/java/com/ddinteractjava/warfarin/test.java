package com.ddinteractjava.warfarin;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import org.hl7.fhir.r4.model.Patient;

public class test {
    public static void main(String[] args) {

        // Create a context
        FhirContext ctx = FhirContext.forR4();

        //Create authorization interceptor
        IClientInterceptor authInterceptor = new BearerTokenAuthInterceptor("12345");

        // Create a client
        IGenericClient client = ctx.newRestfulGenericClient("http://3.21.182.6/omoponfhir4/fhir");
        client.registerInterceptor(authInterceptor);

        // Read a patient with the given ID
        Patient patient = client.read().resource(Patient.class).withId("1498").execute();

        // Print the output
        String string = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
        System.out.println(string);

    }
}
