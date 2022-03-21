package com.fhirintcourse.u2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;


@DisplayName("Tests for U2-L04_1:Create US Core Lab Observation")

class L04_1_Tests {
    
    String ObtainResult(String IdentifierValue,
    String ObservationStatusCode, 
    String ObservationDateTime,
    String ObservationLOINCCode,
    String ObservationLOINCDisplay,
    String ResultType,
    String NumericResultValue,
    String NumericResultUCUMUnit,
    String CodedResultSNOMEDCode,
    String CodedResultSNOMEDDisplay
   )
    {
        String aux="";
        L04_1_CreateUSCoreObservation L04_1 = new L04_1_CreateUSCoreObservation();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
       
        try {
            aux = L04_1.CreateUSCoreR4LabObservation(ServerEndPoint, IdentifierSystem, IdentifierValue
            ,ObservationStatusCode
            ,ObservationDateTime
            ,ObservationLOINCCode
            ,ObservationLOINCDisplay
            ,ResultType
            ,NumericResultValue
            ,NumericResultUCUMUnit
            ,CodedResultSNOMEDCode
            ,CodedResultSNOMEDDisplay
           
            );
            

        } 
        catch (Exception e) {
            aux=e.getMessage();
        }
        
        return aux;
    }
    @Test
    @DisplayName("L04_1_T01:Patient Not Found")
    void L04_1_T01() {

        String IdentifierValue = "L04_1_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String ObservationStatusCode="";
        String ObservationDateTime="";
        String ObservationLOINCCode="";
        String ObservationLOINCDisplay="";
        String ResultType="";
        String NumericResultValue="";
        String NumericResultUCUMUnit="";
        String CodedResultSNOMEDCode="";
        String CodedResultSNOMEDDisplay="";
       
        String result=ObtainResult(IdentifierValue
        ,ObservationStatusCode
        ,ObservationDateTime
        ,ObservationLOINCCode
        ,ObservationLOINCDisplay
        ,ResultType
        ,NumericResultValue
        ,NumericResultUCUMUnit
        ,CodedResultSNOMEDCode
        ,CodedResultSNOMEDDisplay
        );
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L04_1_T02:Create Coded Observation Result")
    void L04_1_T02() {

        String IdentifierValue = "L04_1_T02";
        String ObservationStatusCode="final";
        String ObservationDateTime="2020-10-11T20:30:00Z";
        String ObservationLOINCCode="5778-6";
        String ObservationLOINCDisplay="Color of Urine";
        String ResultType="Coded";
        String NumericResultValue="";
        String NumericResultUCUMUnit="";
        String CodedResultSNOMEDCode="371244009";
        String CodedResultSNOMEDDisplay="Yellow";
    
        
        String result=ObtainResult(IdentifierValue
        ,ObservationStatusCode
        ,ObservationDateTime
        ,ObservationLOINCCode
        ,ObservationLOINCDisplay
        ,ResultType
        ,NumericResultValue
        ,NumericResultUCUMUnit
        ,CodedResultSNOMEDCode
        ,CodedResultSNOMEDDisplay
           );
        
           Config c=new Config();
        String val=ValidateResource(c.ServerEndpoint(), result);
        assertEquals(val,"OK");
        
       
    }
    @Test
    @DisplayName("L04_1_T03:Create Numerical Observation Result")
    void L04_1_T03() {
        
        String IdentifierValue = "L04_1_T02";
        String ObservationStatusCode="final";
        String ObservationDateTime="2020-10-11T20:30:00Z";
        String ObservationLOINCCode="1975-2";
        String ObservationLOINCDisplay="Bilirubin, serum";
        String ResultType="numeric";
        String NumericResultValue="8.6";
        String NumericResultUCUMUnit="mg/dl";
        String CodedResultSNOMEDCode="";
        String CodedResultSNOMEDDisplay="";
        
        String result=ObtainResult(IdentifierValue
        ,ObservationStatusCode
        ,ObservationDateTime
        ,ObservationLOINCCode
        ,ObservationLOINCDisplay
        ,ResultType
        ,NumericResultValue
        ,NumericResultUCUMUnit
        ,CodedResultSNOMEDCode
        ,CodedResultSNOMEDDisplay
           );
        Config c=new Config();
        if (result!="")
        {
            String val=ValidateResource(c.ServerEndpoint(), result);
            assertEquals(val,"OK");
        }
        else
        {
            assertNotEquals(result, "");
        }
        
    }

    private String ValidateResource(String ServerEndPoint,String resourceTextJson)
    {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
        IParser parser = ctx.newJsonParser();  
        Observation ob=parser.parseResource(Observation.class, resourceTextJson); 
        Parameters inParams = new Parameters();  
        {  
            inParams.addParameter().setName("resource").setResource(ob);  
        }  
    
        Parameters outParams = client  
            .operation()  
            .onType(Observation.class)  
            .named("$validate")  
            .withParameters(inParams)  
            .execute();  
    
     OperationOutcome oo = (OperationOutcome) outParams.getParameter().get(0).getResource();  
     String auxE="OK";
     if (!(oo.getIssueFirstRep().getDetails().getText().equals("Validation successful, no issues found")))
            {
                auxE="Error:"+oo.getIssueFirstRep().getDetails().getText();
            }
            
     return auxE;
    }
   
}