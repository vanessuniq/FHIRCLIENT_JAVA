package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;



@DisplayName("Tests for U2-L02_1:GetEthnicity")

class L02_1_Tests {
    
    String ObtainResult(String IdentifierValue)
    {
        String aux="";
        L02_1_FetchEthnicity L02_1 = new L02_1_FetchEthnicity();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L02_1.GetEthnicity(ServerEndPoint, IdentifierSystem, IdentifierValue);
            

        } catch (Exception e) {
            aux=e.getMessage();
            
        }
        return aux;
    }
    @Test
    @DisplayName("L02_1_T01:Patient Not Found")
    void L02_1_T01() {
        String IdentifierValue = "L02_1_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L02_1_T02:No US-CORE Ethnicity Extension")
    void L02_1_T02() {

        String IdentifierValue = "L02_1_T02";
        String ExpectedResult = "Error:No_us-core-ethnicity_Extension";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L02_1_T03:Non Conformant US-Core Ethnicity Extension")
    void L02_1_T03() {
        
        String IdentifierValue = "L02_1_T03";
        String ExpectedResult = "Error:Non_Conformant_us-core-ethnicity_Extension";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L02_1_T04:One Provider in the City")
    void L02_1_T04() {
        String IdentifierValue = "L02_1_T04";
        String ExpectedResult="text|Hispanic or Latino\n";
        ExpectedResult+="code|2135-2:Hispanic or Latino\n";
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L02_1_T05:Fully Populated Ethnicity Extension")
    void L02_1_T05() {
        String IdentifierValue = "L02_1_T05";
        String ExpectedResult="text|Hispanic or Latino\n";
        ExpectedResult+="code|2135-2:Hispanic or Latino\n";
        ExpectedResult+="detail|2184-0:Dominican\n";
        ExpectedResult+="detail|2148-5:Mexican\n";
        ExpectedResult+="detail|2151-9:Chicano\n";
            
        String result=ObtainResult(IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    
   
}