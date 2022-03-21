package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;



@DisplayName("Tests for U2-L01_1:Fetch Demographics")

class L01_1_Tests {
    
    String ObtainResult(String IdentifierValue)
    {
        String aux="";
        L01_1_FetchDemographics L01_1 = new L01_1_FetchDemographics();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L01_1.GetPatientPhoneAndEmail(ServerEndPoint, IdentifierSystem, IdentifierValue);
            

        }
        catch (Exception e) {
           aux = e.getMessage();

        }
    
        return aux;
    }
    @Test
    @DisplayName("L01_1_T01:Patient Not Found")
    void L01_1_T01() {
        String IdentifierValue = "L01_1_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_1_T02:Patient Exists, but has no telecom element")
    void L01_1_T02() {

        String IdentifierValue = "L01_1_T02";
        String ExpectedResult = "Emails:-\nPhones:-\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_1_T03:Patient Exists, only with one phone number")
    void L01_1_T03() {
        
        String IdentifierValue = "L01_1_T03";
        String ExpectedResult = "Emails:-\nPhones:+15555555555(HOME)\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_1_T04:Patient Exists, only with one email address")
    void L01_1_T04() {
        String IdentifierValue = "L01_1_T04";
        String ExpectedResult = "Emails:mymail@patientt04.com(HOME)\nPhones:-\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L01_1_T05:Patient Exists, with more than one phone number and email addresses")
    void L01_1_T05() {
        String IdentifierValue = "L01_1_T05";
        String ExpectedResult = "Emails:mymail@patientt05job.com(WORK),mymail@patientt05.com(Home)\nPhones:+15555555555(WORK),+16666666666(HOME)\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L01_1_T06:Patient Exists, with one telecom which is not phone or email")
    void L01_1_T06() {
        String IdentifierValue = "L01_1_T06";
        String ExpectedResult = "Emails:-\nPhones:-\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }

   
}