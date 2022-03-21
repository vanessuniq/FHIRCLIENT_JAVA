package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;


@DisplayName("Tests for U2-L03_2:GetMedications")

class L03_2_Tests {
    
    String ObtainResult(String IdentifierValue)
    {
        String aux="";
        L03_2_FetchMedication L03_2 = new L03_2_FetchMedication();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L03_2.GetMedications(ServerEndPoint, IdentifierSystem, IdentifierValue);
            

        } 
        catch (Exception e) {
            aux=e.getMessage();
        }
        
        return aux;
    }
    @Test
    @DisplayName("L03_2_T01:Patient Not Found")
    void L03_2_T01() {
        String IdentifierValue = "L03_2_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_2_T02:No Medication Data")
    void L03_2_T02() {

        String IdentifierValue = "L03_2_T02";
        String ExpectedResult = "Error:No_Medications";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_2_T03:One Medication")
    void L03_2_T03() {
        
        String IdentifierValue = "L03_2_T03";
        String ExpectedResult = "Active|Order|2021-01-05|582620:Nizatidine 15 MG/ML Oral Solution [Axid]|John Requester, MD\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_2_T04:Several Medications")
    void L03_2_T04() {
        String IdentifierValue = "L03_2_T04";
        String ExpectedResult="Active|Order|2021-01-05|582620:Nizatidine 15 MG/ML Oral Solution [Axid]|Mary Requesting, MD\n";
                ExpectedResult+="Active|Order|2021-01-05|198436:Acetaminophen 325 MG Oral Capsule|Mary Requesting, MD\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    
   
}