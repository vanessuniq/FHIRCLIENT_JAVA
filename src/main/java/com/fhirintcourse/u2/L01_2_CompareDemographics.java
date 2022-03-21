package com.fhirintcourse.u2;

import java.text.SimpleDateFormat;

import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;

public class L01_2_CompareDemographics {

  public String GetDemographicComparison(String ServerEndPoint, String IdentifierSystem, String IdentifierValue,
      String myFamily, String myGiven, String myGender, String myBirth)

  {
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    String aux = "";
    if (pt != null) {
      String famFormat = "{family}|%s|%s|%s";
      String givenFormat = "{given}|%s|%s|%s";
      String genderFormat = "{gender}|%s|%s|%s";
      String bdFormat = "{birthDate}|%s|%s|%s";
      String matchG = "{green}\n";
      String matchR = "{red}\n";

      String ptFamily = pt.getNameFirstRep().getFamily();
      String testFamily = String.format(famFormat, myFamily, ptFamily, ptFamily.equals(myFamily) ? matchG : matchR);

      String ptGiven = pt.getNameFirstRep().getGivenAsSingleString();
      String testGiven = String.format(givenFormat, myGiven, ptGiven, ptGiven.equals(myGiven) ? matchG : matchR);

      String ptGender = pt.getGender().toCode();
      String testGender = String.format(genderFormat, myGender, ptGender, ptGender.equals(myGender) ? matchG : matchR);

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String ptBd = formatter.format(pt.getBirthDate());
      String testBd = String.format(bdFormat, myBirth, ptBd, ptBd.equals(myBirth) ? matchG : matchR);

      aux = testFamily + testGiven + testGender + testBd;
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

}
