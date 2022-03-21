package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;


@DisplayName("Tests for U2-L03_3:GetIPSClinicalData")

class L03_3_Tests {
    
    String ObtainResult(String IdentifierValue,String Class)
    {
        String aux="";
        L03_3_FetchIPSClinicalData L03_3 = new L03_3_FetchIPSClinicalData();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            if (Class=="medications")
                aux = L03_3.GetIPSMedications(ServerEndPoint, IdentifierSystem, IdentifierValue);
            else
                aux = L03_3.GetIPSImmunizations(ServerEndPoint, IdentifierSystem, IdentifierValue);
            
            

        } 
        
        catch (Exception e) {
            aux=e.getMessage();
        }
        
        return aux;
    }
    @Test
    @DisplayName("L03_3_T01:Patient Not Found")
    void L03_3_T01() {
        String IdentifierValue = "L03_3_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String result=ObtainResult(IdentifierValue,"medications");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_3_T02:No IPS")
    void L03_3_T02() {

        String IdentifierValue = "L03_3_T02";
        String ExpectedResult = "Error:No_IPS";
        String result=ObtainResult(IdentifierValue,"medications");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_3_T03:IPS with Meds, no Imm")
    void L03_3_T03() {
        
        String IdentifierValue = "L03_3_T03";
        String ExpectedResult="Active|2015-03|108774000:Product containing anastrozole (medicinal product)\n";
        ExpectedResult+="Active|2016-01|412588001:Cimicifuga racemosa extract (substance)\n";
        String result=ObtainResult(IdentifierValue,"medications");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L03_3_T04:IPS with No Meds")
    void L03_3_T04() {
        String IdentifierValue = "L03_3_T04";
        String ExpectedResult="Active||no-medication-info:No information about medications\n";
        String result=ObtainResult(IdentifierValue,"medications");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L03_3_T05:IPS with Imm, no Meds")
    void L03_3_T05() {
        String IdentifierValue = "L03_3_T04";
        String ExpectedResult="Completed|1998-06-04T00:00:00+02:00|414005006:Diphtheria + Pertussis + Poliomyelitis + Tetanus vaccine\n";
        String result=ObtainResult(IdentifierValue,"immunizations");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L03_3_T06:IPS with no Imm, Meds")
    void L03_3_T06() {
        String IdentifierValue = "L03_3_T03";
        String ExpectedResult="Error:IPS_No_Immunizations";
        String result=ObtainResult(IdentifierValue,"immunizations");
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    
   
}