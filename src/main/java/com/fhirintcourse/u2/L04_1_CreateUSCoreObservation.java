package com.fhirintcourse.u2;

import java.math.BigDecimal;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class L04_1_CreateUSCoreObservation {

  public String CreateUSCoreR4LabObservation(
      String ServerEndPoint,
      String IdentifierSystem,
      String IdentifierValue,
      String ObservationStatusCode,
      String ObservationDateTime,
      String ObservationLOINCCode,
      String ObservationLOINCDisplay,
      String ResultType,
      String NumericResultValue,
      String NumericResultUCUMUnit,
      String CodedResultSNOMEDCode,
      String CodedResultSNOMEDDisplay) {

    String aux = "";
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    if (pt != null) {
      Observation observation = new Observation();
      // Add profile
      observation.getMeta().addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-observation-lab");
      // Add Text
      observation.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("Redacted");
      // Add status (code)
      observation.setStatus(ObservationStatus.fromCode(ObservationStatusCode));
      // Add category
      CodeableConcept cCategory = new CodeableConcept();
      cCategory.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/observation-category")
          .setCode("laboratory").setDisplay("Laboratory");
      cCategory.setText("Laboratory");
      observation.addCategory(cCategory);
      // Add code (codeableconcept)
      CodeableConcept code = new CodeableConcept();
      code.addCoding().setSystem("http://loinc.org").setCode(ObservationLOINCCode)
          .setDisplay(ObservationLOINCDisplay);
      observation.setCode(code);
      // Add subject
      Reference subject = new Reference(new IdType(pt.getId()).getId());
      subject.setDisplay(pt.getNameFirstRep().getNameAsSingleString());
      observation.setSubject(subject);
      // Add effectiveDateTime
      observation.setEffective(new DateTimeType(ObservationDateTime));
      // Add value
      if (ResultType.equals("numeric")) {
        Quantity value = new Quantity();
        value.setValue(new BigDecimal(NumericResultValue)).setUnit(NumericResultUCUMUnit);
        observation.setValue(value);
      } else {
        CodeableConcept value = new CodeableConcept();
        value.addCoding().setCode(CodedResultSNOMEDCode).setDisplay(CodedResultSNOMEDDisplay)
            .setSystem("http://snomed.info/sct");
        observation.setValue(value);
      }
      // Encode resource in JSON
      FhirContext ctx = FhirContext.forR4();
      IParser parser = ctx.newJsonParser();
      aux = parser.encodeResourceToString(observation);
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

}
