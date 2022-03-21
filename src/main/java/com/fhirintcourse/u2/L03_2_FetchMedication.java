package com.fhirintcourse.u2;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Coding;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class L03_2_FetchMedication {

  public String GetMedications(String ServerEndPoint, String IdentifierSystem, String IdentifierValue)

  {
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    String aux = "";
    if (pt != null) {
      String ptId = new IdType(ptEntry.getFullUrl()).getIdPart();
      List<BundleEntryComponent> entries = getMedicationEntries(ServerEndPoint, ptId);
      if (!entries.isEmpty()) {
        for (BundleEntryComponent entry : entries) {
          MedicationRequest medRequest = (MedicationRequest) entry.getResource();
          String status = medRequest.getStatus().getDisplay();
          String intent = medRequest.getIntent().getDisplay();
          SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
          String authoredOn = formater.format(medRequest.getAuthoredOn());
          Coding coding = medRequest.getMedicationCodeableConcept().getCodingFirstRep();
          String code = coding.getCode();
          String medName = coding.getDisplay();
          String requester = medRequest.getRequester().getDisplay();
          aux += String.format("%s|%s|%s|%s:%s|%s%n", status, intent, authoredOn, code, medName, requester);
        }
      } else {
        aux = "Error:No_Medications";
      }
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

  private List<BundleEntryComponent> getMedicationEntries(String server, String patientId) {
    IGenericClient client = new L01_1_FetchDemographics().getClient(server);
    String searchParam = "MedicationRequest?patient=" + patientId;
    return client.search()
        .byUrl(searchParam)
        .returnBundle(Bundle.class)
        .execute()
        .getEntry();
  }
}
