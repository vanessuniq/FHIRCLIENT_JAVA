package com.fhirintcourse.u2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;



@DisplayName("Tests for U2-L05_1:Terminology Services")

class L05_1_Tests {
    
    String ObtainResult(String url,
    String filter)
    
    {
        String aux="";
        L05_1_TerminologyService L05_1 = new L05_1_TerminologyService() ;
        Config c=new Config();
        String ServerEndPoint = c.TerminologyServerEndpoint();
        
       
        try {
            aux = L05_1.ExpandValuesetForCombo(ServerEndPoint, url,filter)
            ;
            

        } 
        catch (Exception e) {
            aux=e.getMessage();
        }
        
        return aux;
    }
    @Test
    @DisplayName("L05_1_T01:Term/Concept Not Found")
    void L05_1_T01() {

        String ExpectedResult="Error:ValueSet_Filter_Not_Found";
        String url="http://snomed.info/sct?fhir_vs=isa/73211009";
        String filter="diaxetes";
        
        String result=ObtainResult(url,filter);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L05_1_T02:Term Found")
    void L05_1_T02() {

        String ExpectedResult="5368009|Drug-induced diabetes mellitus\n";
        String url="http://snomed.info/sct?fhir_vs=isa/73211009";
        String filter="Drug-induced diabetes";
        String result=ObtainResult(url,filter);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
        
       
    }
    
}