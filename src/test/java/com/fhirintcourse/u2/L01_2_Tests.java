package com.fhirintcourse.u2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;



@DisplayName("Tests for U2-L01_2:Compare Demographics")

class L01_2_Tests {
    
    String ObtainResult(String IdentifierValue,String myFamily,String myGiven,String myGender,String myBirth)
    {

        String aux="";
        L01_2_CompareDemographics L01_2 = new L01_2_CompareDemographics();
        Config c=new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L01_2.GetDemographicComparison(ServerEndPoint, IdentifierSystem, IdentifierValue,
            myFamily, myGiven, myGender, myBirth);
            
            

        
            } catch (Exception e) {
                aux = e.getMessage();

            }
            return aux;
    }
    @Test
    @DisplayName("L01_2_T01:Patient Not Found")
    void L01_2_T01() {
        String IdentifierValue = "L01_2_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String myFamily="";
        String myGiven="";
        String myGender="";
        String myBirth="";
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_2_T02:Family Name Does Not Match")
    void L01_2_T02() {

        String IdentifierValue = "L01_2_T02";
        String ExpectedResult ="{family}|Dougras|Douglas|{red}\n";
        ExpectedResult+="{given}|Jamieson Harris|Jamieson Harris|{green}\n";
        ExpectedResult+="{gender}|MALE|MALE|{green}\n";
        ExpectedResult+="{birthDate}|1968-07-23|1968-07-23|{green}\n";
        String myFamily="Dougras";
        String myGiven="Jamieson Harris";
        String myGender="male";
        String myBirth="1968-07-23";
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_2_T03:Given Name Does Not Match")
    void L01_2_T03() {
        
        String IdentifierValue = "L01_2_T02";
      
        String ExpectedResult ="{family}|Douglas|Douglas|{green}\n";
        ExpectedResult +="{given}|Jamieson|Jamieson Harris|{red}\n";
        ExpectedResult +="{gender}|MALE|MALE|{green}\n";
        ExpectedResult +="{birthDate}|1968-07-23|1968-07-23|{green}\n";
            
            String myFamily="Douglas";
            String myGiven="Jamieson";
            String myGender="male";
            String myBirth="1968-07-23";
            
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());
    }
    @Test
    @DisplayName("L01_2_T04:Gender Does Not Match")
    void L01_2_T04() {
        String IdentifierValue = "L01_2_T02";
      
        String ExpectedResult ="{family}|Douglas|Douglas|{green}\n";
        ExpectedResult +="{given}|Jamieson Harris|Jamieson Harris|{green}\n";
        ExpectedResult +="{gender}|MALE|MALE|{green}\n";
        ExpectedResult +="{birthDate}|1968-07-24|1968-07-23|{red}\n";
        String myFamily="Douglas";
        String myGiven="Jamieson Harris";
        String myGender="male";
        String myBirth="1968-07-24";
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L01_2_T05:Birth Date Does Not Match")
    void L01_2_T05() {
       
        String IdentifierValue = "L01_2_T02";
         
        String ExpectedResult ="{family}|Douglas|Douglas|{green}\n";
        ExpectedResult+="{given}|Jamieson Harris|Jamieson Harris|{green}\n";
        ExpectedResult+="{gender}|FEMALE|MALE|{red}\n";
        ExpectedResult+="{birthDate}|1968-07-23|1968-07-23|{green}\n";
        String myFamily="Douglas";
        String myGiven="Jamieson Harris";
        String myGender="female";
        String myBirth="1968-07-23";
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }
    @Test
    @DisplayName("L01_2_T06:All the demographic fields match")
    void L01_2_T06() {
        String IdentifierValue = "L01_2_T02";
        String ExpectedResult ="{family}|Douglas|Douglas|{green}\n";
        ExpectedResult+="{given}|Jamieson Harris|Jamieson Harris|{green}\n";
        ExpectedResult+="{gender}|MALE|MALE|{green}\n";
        ExpectedResult+="{birthDate}|1968-07-23|1968-07-23|{green}\n";
        String myFamily="Douglas";
        String myGiven="Jamieson Harris";
        String myGender="male";
        String myBirth="1968-07-23";
        String result=ObtainResult(IdentifierValue,myFamily,myGiven,myGender,myBirth);
        assertEquals(ExpectedResult.toUpperCase(),result.toUpperCase());

    }

   
}