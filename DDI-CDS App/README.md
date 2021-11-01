DDI-CDS Application Development
=
Resources
-

###application.properties
- server.port: The port you want the application to run on
- app.fhirVersion: The FHIR version you wish to use during runtime, can be either `r4` or `stu2`.
- app.redirectUrl: The redirect URI used during authentication and specified during registration. Should point to 
the controller of the ddi.
- app.clientId: The ID generated for the application after registration and used when requesting an access token.
- app.scope: Must describe the access that the app needs, including clinical data scopes like patient/*.read, openid and fhirUser (if app needs authenticated patient identity) and either:
  - a launch value indicating that the app wants to receive already-established launch context details from the EHR 
  - a set of launch context requirements in the form launch/patient, which asks the EHR to establish context on your behalf.
- app.authorizeUrl: The EHR's authorization endpoint. This can normally be obtained by visiting the `/metadata` endpoint of the EHR.
- app.tokenUrl: The EHR's token endpoint. This can normally be obtained by visiting the `/metadata` endpoint of the EHR.
- app.fhirUrl: The EHR's FHIR endpoint. This can normally be obtained by visiting the `/metadata` endpoint of the EHR.
- app.staticToken: A static token that replaces the need for authentication with the EHR. **This should not be set unless it is being used**. Utilized primarily for OMOPonFHIR setup and allows for standalone launches of the application.

###`DDI` property files
Looking at `cyp3a4.properties` we can see 5 different properties being set. These are processed by `CYP3A4Config.java` 
to be used throughout the rest of the application.<br>
Currently there is support for two types of properties:
- Risk factors: Setting the name of the risk factor that will be used throughout the application and the frontend view
- VSAC value set URLs: Used to build a local cache of the value set and then can be used to identify specific use cases of the DDI

JAVA Files
-
##config
###AppConfig
This file is responsible to processing everything set in `application.properties`. Depending upon the `app.fhirVersion` 
set this file will instantiate either `STU2ServiceImplementation` or `R4ServiceImplementation`.

###SecurityConfig
This file allows you to configure Spring Security without the use of XML. As no authorization is needed to access the application this can be largely ignored.

###`DDI` Config
If you establish any `DDI` config files, you will need to create a JAVA file to access the properties defined.

##Controllers
###LaunchController
This file is responsible for facilitating the SMART App Launch. The `\launchStandalone` endpoint requires two query parameters:
- `ddi`: Used to redirect to the proper controller that handles that specific DDI
- `patient`: The patient ID as it exists in the EHR

The `\launch` and `\redirect\` endpoints should not need to be altered unless the SMART app launch sequence is ever updated.<br>
**The `\redirect` endpoint requires the `ddi` query parameter so ensure that this is listed correctly during application
registration with an EHR.**

#Models
###Alternative
Class that holds information regarding the drug that triggers a specific use case in the drug-drug interaction algorithm.
In the case of Colchicine - CYP3A4, this object would contain the drug information (name and code) of the CYP3A4 that the
patient was on and the recommendation that shows up under the "Alternative Options" block on the main page of the web 
application.

###RiskFactor
Class that holds information regarding the risk factors that a patient has based on the results from the /MedicationStatement
and /MedicationRequest queries.
- riskName = The name of the risk factor from the properties file
- hasRiskFactor = Whether the patient has the risk factor based on the FHIR queries
- resourceName = The name of the medication/observation/etc. that is the risk factor for the patient (not currently used)
- effectiveDate = The date the medication/observation/etc. took effect
- value = The value for the medication/observation/etc. (is primarily used for eGFR in Colchicine-CYP3A4)

###SimpleCode
Class that holds information regarding the primary interacting drug in the DDI for that specific patient. In the case of
Colchicine-CYP3A4 this object would contain drug information of the colchicine the patient was on.

###Summary
Class that hold information for the "Summary" block on the main page of the web application.

#Services
###FHIRService
This interface gets implemented by `R4ServiceImplementation` and `STU2ServiceImplementation`. Both classes utilize HAPI 
FHIR and are responsible for all FHIR calls.

###ResourceService
This service contains code that is utilized in the DDI controllers. After performing FHIR requests in the `FHIRService`
implementations, the results are stored in memory and can be referenced via this service. 

###RxNormService/VSACService
These services are responsible for handling requests to `RxNAV` and `VSAC`. `RxNormService`'s primary role is to get the
primary ingredient based off of a given CUI. `VSACService`'s primary role is to create a local cache in memory of the
value sets specified in the properties file. 

Developing a new DDI CDS App
-
- Copy and modify the `CYP3A4` directory inside the `valueset` directory in this project to create the necessary value 
sets for the DDI 
- Create a properties file for the new DDI and specify the URL to the value sets on the VSAC. Also specify any risk
factors here.
- Create a new `Cache` file similar to `CYP3A4Cache.java` where you will create a local cache on the value sets and
specify various use cases and alternatives for the DDI
- In `DDICDSApp.java` add the newly created cache file's instantiation method so it will be run on start-up
- Create a controller similar to `CYP3A4Controller.java` where you will specify how to process the risk factors, the
various use cases of the DDI including the alternative drug options, and the text for the boxes that appear in the 
front-end
- Modify or create additional template `html` files under `/resources/templates` to support the front-end view
  (ideally not much other than creating a new accordion file will have to be done)


