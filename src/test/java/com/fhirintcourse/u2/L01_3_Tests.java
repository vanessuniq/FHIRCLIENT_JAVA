package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;



@DisplayName("Tests for U2-L01_3:GetProvidersNearPatient")

class L01_3_Tests {
    
    String ObtainResult(String IdentifierValue)
    {
        String aux="";
        L01_3_GetProvidersNearPatient L01_3 = new L01_3_GetProvidersNearPatient();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L01_3.GetProvidersNearCity(ServerEndPoint, IdentifierSystem, IdentifierValue);
            

        } catch (Exception e) {
            aux=e.getMessage();
        }
        return aux;
    }
    @Test
    @DisplayName("L01_3_T01:Patient Not Found")
    void L01_3_T01() {
        String IdentifierValue = "L01_3_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_3_T02:Patient Exists, no city element")
    void L01_3_T02() {

        String IdentifierValue = "L01_3_T02";
        String ExpectedResult = "Error:Patient_w/o_City";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_3_T03:Patient Exists, No Provider in the City")
    void L01_3_T03() {
        
        String IdentifierValue = "L01_3_T03";
        String ExpectedResult = "Error:No_Provider_In_Patient_City";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_3_T04:One Provider in the City")
    void L01_3_T04() {
        String IdentifierValue = "L01_3_T04";
        String ExpectedResult = "OnlyPhysician,InTown|Phone:+402-772-7777|2000 ONE PROVIDER DRIVE|OB/GYN\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L01_3_T05:Several providers in the city")
    void L01_3_T05() {
        String IdentifierValue = "L01_3_T05";
        String ExpectedResult = "OnePhysician,First|Phone:+402-772-7777|2000 ONE PROVIDER DRIVE|OB/GYN\nTwoPhysician,Second|Phone:+403-772-7777|3000 TWO PROVIDER DRIVE|FAMILY MEDICINE\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    
   
}