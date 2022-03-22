package com.fhirintcourse.u2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r5.model.IdType;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class L03_3_FetchIPSClinicalData {

  public String GetIPSMedications(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {
    String aux = "";
    Map<String, Object> ptMap = getPatientandId(ServerEndPoint, IdentifierSystem, IdentifierValue);
    Patient pt = (Patient) ptMap.get("patient");
    String patientId = (String) ptMap.get("patientId");
    if (pt != null) {
      List<BundleEntryComponent> ips = getIPSDodcument(ServerEndPoint, patientId);
      if (!ips.isEmpty()) {
        Bundle bundle = (Bundle) ips.get(0).getResource();
        for (BundleEntryComponent entry : bundle.getEntry()) {
          // "m_status+|m_date_period|m_code:m_display"
          String status = "";
          String period = "";
          String code = "no-medication-info";
          String display = "No information about medications";
          if (entry.getResource().getResourceType().toString().equals("MedicationStatement")) {
            MedicationStatement mStatement = (MedicationStatement) entry.getResource();
            status = mStatement.getStatus().getDisplay();
            Coding c = null;
            if (mStatement.hasMedicationCodeableConcept()) {
              c = mStatement.getMedicationCodeableConcept().getCodingFirstRep();
            } else {
              period = mStatement.getEffectivePeriod().getStartElement().getValueAsString();
              String medId = mStatement.getMedicationReference().getReferenceElement().getIdPart();
              BundleEntryComponent medEntry = bundle.getEntry().stream().filter(p -> p.getFullUrl().contains(medId))
                  .findFirst().get();
              Medication med = (Medication) medEntry.getResource();
              c = med.getCode().getCodingFirstRep();
            }
            code = c.getCode();
            display = c.getDisplay();

            aux += String.format("%s|%s|%s:%s%n", status, period, code, display);
          }

        }
      } else {
        aux = "Error:No_IPS";
      }
    } else {
      aux = "Error:Patient_Not_Found";
    }

    return aux;
  }

  public String GetIPSImmunizations(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {
    String aux = "";
    Map<String, Object> ptMap = getPatientandId(ServerEndPoint, IdentifierSystem, IdentifierValue);
    Patient pt = (Patient) ptMap.get("patient");
    String patientId = (String) ptMap.get("patientId");
    if (pt != null) {
      List<BundleEntryComponent> ips = getIPSDodcument(ServerEndPoint, patientId);
      if (!ips.isEmpty()) {
        Bundle bundle = (Bundle) ips.get(0).getResource();
        for (BundleEntryComponent entry : bundle.getEntry()) {
          // i_status+|imm_date|i_code:i_display
          if (entry.getResource().getResourceType().toString().equals("Immunization")) {
            Immunization immunization = (Immunization) entry.getResource();
            String status = immunization.getStatus().getDisplay();
            String date = immunization.getOccurrenceDateTimeType().getValueAsString();
            Coding coding = immunization.getVaccineCode().getCodingFirstRep();
            String code = coding.getCode();
            String display = coding.getDisplay();
            aux += String.format("%s|%s|%s:%s%n", status, date, code, display);
          }
        }
        if (aux.isEmpty())
          aux = "Error:IPS_No_Immunizations";
      } else {
        aux = "Error:No_IPS";
      }
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

  public List<BundleEntryComponent> getIPSDodcument(String server, String patientId) {
    IGenericClient client = new L01_1_FetchDemographics().getClient(server);
    String searchParam = "Bundle?composition.patient=" + patientId;
    return client.search()
        .byUrl(searchParam)
        .returnBundle(Bundle.class)
        .execute()
        .getEntry();
  }

  private Map<String, Object> getPatientandId(String server, String patientIdentifierSystem,
      String patientIdentifierValue) {
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(server, patientIdentifierSystem,
        patientIdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    String ptId = ptEntry == null ? null : new IdType(ptEntry.getFullUrl()).getIdPart();
    Map<String, Object> ptMap = new HashMap<String, Object>();
    ptMap.put("patient", pt);
    ptMap.put("patientId", ptId);
    return ptMap;
  }

}
