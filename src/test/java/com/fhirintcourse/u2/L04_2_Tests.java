package com.fhirintcourse.u2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;


@DisplayName("Tests for U2-L04_2:Create US Core Immunization")

class L04_2_Tests {
    
    String ObtainResult(String IdentifierValue,
    String ImmunizationStatusCode, 
    String ImmunizationDateTime,
    String ProductCVXCode, 
    String ProductCVXDisplay,
    String ReasonCode)
    
    {
        String aux="";
        L04_2_CreateUSCoreImmunization L04_2 = new L04_2_CreateUSCoreImmunization();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
       
        try {
            aux = L04_2.CreateUSCoreR4Immunization(ServerEndPoint, IdentifierSystem, IdentifierValue
            , ImmunizationStatusCode, ImmunizationDateTime, ProductCVXCode, ProductCVXDisplay, ReasonCode)
            ;
            

        } 
        catch (Exception e) {
            aux=e.getMessage();
        }
        
        return aux;
    }
    @Test
    @DisplayName("L04_2_T01:Patient Not Found")
    void L04_2_T01() {

        String IdentifierValue = "L04_2_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String ImmunizationStatusCode="";
        String ImmunizationDateTime="";
        String ProductCVXCode="";
        String ProductCVXDisplay="";
        String ReasonCode="";
        String result=ObtainResult(IdentifierValue,
        ImmunizationStatusCode, ImmunizationDateTime, ProductCVXCode, ProductCVXDisplay, ReasonCode)
        ;
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L04_2_T02:Immunization Completed")
    void L04_2_T02() {

        String IdentifierValue = "L04_2_T02";
        String ImmunizationStatusCode="completed";
        String ImmunizationDateTime="2021-10-25";
        String ProductCVXCode="173";
        String ProductCVXDisplay="cholera, BivWC";
        String ReasonCode="";
        String result=ObtainResult(IdentifierValue,
        ImmunizationStatusCode, ImmunizationDateTime, ProductCVXCode, ProductCVXDisplay, ReasonCode)
        ;
      
        Config c=new Config();
        String val=ValidateResource(c.ServerEndpoint(), result);
        assertEquals(val,"OK");
        
       
    }
    @Test
    @DisplayName("L04_2_T03:Immunization Not Done")
    void L04_2_T03() {
        
        String IdentifierValue = "L04_2_T02";
        String ImmunizationStatusCode="not-done";
        String ImmunizationDateTime="2021-10-30T10:30:00Z";
        String ProductCVXCode="207";
        String ProductCVXDisplay="COVID-19, mRNA, LNP-S, PF, 100 mcg/0.5 mL dose";
        String ReasonCode="IMMUNE";
    
        String result=ObtainResult(IdentifierValue,
        ImmunizationStatusCode, ImmunizationDateTime, ProductCVXCode, ProductCVXDisplay, ReasonCode)
        ;
      
        Config c=new Config();
        String val=ValidateResource(c.ServerEndpoint(), result);
        assertEquals(val,"OK");
    }

    private String ValidateResource(String ServerEndPoint,String resourceTextJson)
    {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
        IParser parser = ctx.newJsonParser();  
        Immunization ob=parser.parseResource(Immunization.class, resourceTextJson); 
        Parameters inParams = new Parameters();  
        {  
            inParams.addParameter().setName("resource").setResource(ob);  
        }  
    
        Parameters outParams = client  
            .operation()  
            .onType(Immunization.class)  
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