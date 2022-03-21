package com.fhirintcourse.u2;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r5.model.IdType;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class L03_1_FetchImmunization {

  public String GetImmunizations(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {

    String aux = "";
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    if (pt != null) {
      String ptId = new IdType(ptEntry.getFullUrl()).getIdPart();
      List<BundleEntryComponent> entries = getPatientImmunizations(ServerEndPoint,
          ptId);
      if (!entries.isEmpty()) {
        for (BundleEntryComponent entry : entries) {
          Immunization immunization = (Immunization) entry.getResource();
          String status = immunization.getStatus().getDisplay();
          Coding coding = immunization.getVaccineCode().getCodingFirstRep();
          String code = coding.getCode();
          String display = coding.getDisplay();
          SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
          String date = formater.format(immunization.getOccurrenceDateTimeType().getValue());
          aux += String.format("%s|%s:%s|%s%n", status, code, display, date);
        }
      } else {
        aux = "Error:No_Immunizations";
      }
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

  private List<BundleEntryComponent> getPatientImmunizations(String server, String id) {
    String searchParam = "Immunization?patient=" + id;
    IGenericClient client = new L01_1_FetchDemographics().getClient(server);

    return client.search()
        .byUrl(searchParam)
        .returnBundle(Bundle.class)
        .execute()
        .getEntry();
  }

}
