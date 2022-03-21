package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;


@DisplayName("Tests for U2-L03_1:GetImmunizations")

class L03_1_Tests {
    
    String ObtainResult(String IdentifierValue)
    {
        String aux="";
        L03_1_FetchImmunization L03_1 = new L03_1_FetchImmunization();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L03_1.GetImmunizations(ServerEndPoint, IdentifierSystem, IdentifierValue);
            

        } 
        catch (Exception e) {
            aux=e.getMessage();
        }
        return aux;
    }
    @Test
    @DisplayName("L03_1_T01:Patient Not Found")
    void L03_1_T01() {
        String IdentifierValue = "L03_1_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_1_T02:No Immunization Data")
    void L03_1_T02() {

        String IdentifierValue = "L03_1_T02";
        String ExpectedResult = "Error:No_Immunizations";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_1_T03:One Immunization")
    void L03_1_T03() {
        
        String IdentifierValue = "L03_1_T03";
        String ExpectedResult = "Completed|158:influenza, injectable, quadrivalent|2020-01-08\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_1_T04:Several Immunizations")
    void L03_1_T04() {
        String IdentifierValue = "L03_1_T04";
        String ExpectedResult="Completed|207:COVID-19, mRNA, LNP-S, PF, 100 mcg/0.5 mL dose|2020-01-10\n";
               ExpectedResult+="Completed|173:cholera, BivWC|2019-10-20\n";
            
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    
   
}