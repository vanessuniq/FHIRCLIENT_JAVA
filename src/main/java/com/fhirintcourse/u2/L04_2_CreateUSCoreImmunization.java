package com.fhirintcourse.u2;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r5.model.IdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class L04_2_CreateUSCoreImmunization {

  public String CreateUSCoreR4Immunization(
      String ServerEndPoint,
      String IdentifierSystem,
      String IdentifierValue,
      String ImmunizationStatusCode,
      String ImmunizationDateTime,
      String ProductCVXCode,
      String ProductCVXDisplay,
      String ReasonCode)

  {
    String aux = "";
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    if (pt != null) {
      Immunization immunization = new Immunization();
      // Set profile
      immunization.getMeta().addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization");
      // Add narrative
      immunization.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("Redacted");
      // Status code
      immunization.setStatus(ImmunizationStatus.fromCode(ImmunizationStatusCode));
      // Status reason
      if (!ReasonCode.isEmpty()) {
        CodeableConcept value = new CodeableConcept();
        value.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-ActReason").setCode(ReasonCode);
        immunization.setStatusReason(value);
      }
      // Vaccine code
      CodeableConcept vaccineCode = new CodeableConcept();
      vaccineCode.addCoding().setCode(ProductCVXCode).setDisplay(ProductCVXDisplay)
          .setSystem("http://hl7.org/fhir/sid/cvx");
      immunization.setVaccineCode(vaccineCode);
      // OccurrenceDateTime
      immunization.setOccurrence(new DateTimeType(ImmunizationDateTime));
      // Patient
      Reference patient = new Reference(new IdType(pt.getId()).getId());
      patient.setDisplay(pt.getNameFirstRep().getNameAsSingleString());
      immunization.setPatient(patient);
      // primary source
      immunization.setPrimarySource(false);

      // Encode in json
      FhirContext ctx = FhirContext.forR4();
      IParser parser = ctx.newJsonParser();
      aux = parser.encodeResourceToString(immunization);
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

}
