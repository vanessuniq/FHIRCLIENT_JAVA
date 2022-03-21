package com.fhirintcourse.u2;

// This module is a demo in node js, created to understand different ways to query a FHIR Server
// and retrieve/create Patient Demographic Information and US Core/IPS conformant resources
// You are free to copy code from this demos to create the functions for your assignment submission.
// (copying from here is not mandatory, though)
// We will demo:
// 1) How to get a Patient's full name and address
// 2) How to get a US Core Race extension
// 3) How to read all US Core Condition resources for a patient
// 4) How to create a US Core Allergy conformant resource
// 5) How to read lab results from an IPS document for a patient
// 6) How to expand  a valueset using a terminology server
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceCriticality;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceReactionComponent;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceType;
import org.hl7.fhir.r4.model.AllergyIntolerance.AllergyIntoleranceSeverity;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

public class L00_0_Demo {

  private String GetGiven(Patient p) {
    String te = p.getNameFirstRep().getGivenAsSingleString();
    return te;
  }

  private String GetFamily(Patient p) {

    String te = p.getNameFirstRep().getFamily();
    return te;
  }

  public String L00_1_GetPatientFullNameAndAddress(
      String server,
      String patientidentifiersystem,
      String patientidentifiervalue)

  {
    String auxA = "";
    String auxN = "";
    String aux = "Error:Patient_Not_Found";
    Patient pa = GetPatient(server, patientidentifiersystem, patientidentifiervalue);
    if (pa != null) {
      auxN = GetFamily(pa) + "," + GetGiven(pa);
      List<Address> add = pa.getAddress();

      for (Address xad : add) {
        String paddr = "";
        List<StringType> lns = xad.getLine();
        for (StringType l : lns) {
          paddr += l.toString() + " ";
        }

        if (xad.hasCity()) {
          paddr = paddr + " - " + xad.getCity();
        }
        if (xad.hasState()) {
          paddr = paddr + " , " + xad.getState();
        }
        if (xad.hasCountry()) {
          paddr = paddr + " , " + xad.getCountry();
        }
        if (xad.hasPostalCode()) {
          paddr = paddr + " (" + xad.getPostalCode() + ")";
        }

        auxA = auxA + paddr + " / ";

      }
      if (auxN == "") {
        auxN = "-";
      }
      if (auxA == "") {
        auxA = "-";
      }
      aux = "Full Name:" + auxN + "\n" + "Address:" + auxA + "\n";

    }

    return aux;

  }

  private Patient GetPatient(String server, String patientidentifiersystem, String patientidentifiervalue) {
    // Create a context
    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
    // Create a client
    IGenericClient client = ctx.newRestfulGenericClient(server);
    Patient pa = null;

    Bundle bu = client
        .search()
        .forResource((Patient.class))
        .and(Patient.IDENTIFIER.exactly().systemAndCode(patientidentifiersystem, patientidentifiervalue))
        .returnBundle(Bundle.class)
        .execute();

    for (Bundle.BundleEntryComponent entry : bu.getEntry()) {
      if (entry.getResource().fhirType().toString() == "Patient") {
        pa = (Patient) entry.getResource();
        break;
      }
    }
    return pa;

  }

  private String GetRaceExtension(Patient p) {
    String raceExtensionUrl = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-race";
    String aux = "Error:No_us-core-race_Extension";
    String auxt = "";
    String auxo = "";
    String auxd = "";

    if (p.hasExtension(raceExtensionUrl)) {
      Extension e = p.getExtensionByUrl(raceExtensionUrl);
      if (e.hasExtension())

      {

        aux = "";
        List<Extension> el = e.getExtension();
        for (Extension efs : el) {
          String url = efs.getUrl();
          if (url.equals("text")) {
            auxt = "text|" + efs.getValue() + "\n";

          } else {
            if (url.equals("ombCategory")) {
              Coding c = (Coding) efs.getValue();
              auxo = "code|" + c.getCode() + ":" + c.getDisplay() + "\n";
            } else {
              if (url.equals("detailed")) {
                Coding c = (Coding) efs.getValue();
                auxd += "detail|" + c.getCode() + ":" + c.getDisplay() + "\n";

              }
            }

          }

          aux = "";
          aux = aux + auxt;
          aux = aux + auxo;
          aux = aux + auxd;

        }

      }
      // Check Mandatory Items
      if ((auxt == "") || (auxo == "")) {
        aux = "Error:Non_Conformant_us-core-race_Extension";
      }

    }
    return aux;

  }

  public String L00_2_GetUSCoreRace(
      String server,
      String patientidentifiersystem,
      String patientidentifiervalue) {
    Patient p = GetPatient(server, patientidentifiersystem, patientidentifiervalue);
    String aux = "Error:Patient_Not_Found";
    if (p != null) {
      aux = GetRaceExtension(p);
    }
    return aux;
  }

  public String L00_3_GetConditions(
      String server,
      String patientidentifiersystem,
      String patientidentifiervalue)

  {
    String aux = "Error:Patient_Not_Found";

    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
    // Create a client
    IGenericClient client = ctx.newRestfulGenericClient(server);
    Patient pa = null;
    Bundle.BundleEntryComponent en = null;
    Bundle bu = client
        .search()
        .forResource((Patient.class))
        .and(Patient.IDENTIFIER.exactly().systemAndCode(patientidentifiersystem, patientidentifiervalue))
        .returnBundle(Bundle.class)
        .execute();

    for (Bundle.BundleEntryComponent entry : bu.getEntry()) {
      if (entry.getResource().fhirType().toString() == "Patient") {
        en = entry;
        pa = (Patient) entry.getResource();
        break;
      }
    }

    if (pa != null) {

      String FullId = en.getFullUrl();
      URL aURL;
      try {
        aURL = new URL(FullId);

        String id;
        // Make sure I've got only the id
        // from the server/resource/id construct
        String[] segments = aURL.getPath().split("/");
        id = segments[segments.length - 1];

        aux = GetConditions(server, id);
      } catch (MalformedURLException e) {
        aux = e.getMessage();

      }

    }

    return aux;
  }

  private String GetConditions(String server, String id) {
    String aux = "";
    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
    // Create a client
    IGenericClient client = ctx.newRestfulGenericClient(server);

    Bundle bu = client.search()
        .forResource((Condition.class))
        .and(Condition.PATIENT.hasId(id))
        .returnBundle(Bundle.class)
        .execute();

    for (Bundle.BundleEntryComponent entry : bu.getEntry()) {
      Condition oneI = (Condition) entry.getResource();

      String cStatus = oneI.getClinicalStatus().getCodingFirstRep().getDisplay();
      String cVerification = oneI.getVerificationStatus().getCodingFirstRep().getDisplay();
      String cCategory = oneI.getCategoryFirstRep().getCodingFirstRep().getDisplay();
      String cCode = oneI.getCode().getCodingFirstRep().getCode() + ":"
          + oneI.getCode().getCodingFirstRep().getDisplay();
      DateTimeType t = oneI.getOnsetDateTimeType();
      String cDate = t.toHumanDisplay();
      aux += cStatus + "|" + cVerification + "|" + cCategory + "|" + cCode + "|" + cDate + "\n";

    }

    if (aux == "") {
      aux = "Error:No_Conditions";
    }
    return aux;
  }

  public String L00_4_CreateUSCoreAllergyIntolerance(
      String server,
      String patientidentifiersystem,
      String patientidentifiervalue,
      String ClinicalStatusCode,
      String VerificationStatusCode,
      String CategoryCode,
      String CriticalityCode,
      String AllergySnomedCode,
      String AllergySnomedDisplay,
      String ManifestationSnomedCode,
      String ManifestationSnomedDisplay,
      String ManifestationSeverityCode) {

    Patient patient = GetPatient(server, patientidentifiersystem, patientidentifiervalue);
    String aux = "Error:Patient_Not_Found";
    if (patient != null) {

      AllergyIntolerance a = new AllergyIntolerance();

      // Profile
      a.getMeta().addProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance");
      // Text
      a.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("Redacted");
      CodeableConcept cClinicalStatus = new CodeableConcept();
      // Type
      a.setType(AllergyIntoleranceType.ALLERGY);
      // Clinical Status
      cClinicalStatus.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical")
          .setCode(ClinicalStatusCode);
      a.setClinicalStatus(cClinicalStatus);
      // Verification Status
      CodeableConcept cVerificationStatus = new CodeableConcept();
      cVerificationStatus.addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification")
          .setCode(VerificationStatusCode);
      a.setVerificationStatus(cVerificationStatus);
      // Category
      a.addCategory(AllergyIntolerance.AllergyIntoleranceCategory.fromCode(CategoryCode));
      // Criticality
      a.setCriticality(AllergyIntoleranceCriticality.fromCode(CriticalityCode));

      // Code
      CodeableConcept cCode = new CodeableConcept();
      cCode.addCoding().setSystem("http://snomed.info/sct").setCode(AllergySnomedCode).setDisplay(AllergySnomedDisplay);
      cCode.setText(AllergySnomedDisplay);
      a.setCode(cCode);

      // Patient reference
      Reference r = new Reference();
      r.setDisplay(patient.getNameFirstRep().getNameAsSingleString());
      IdType iid = new IdType();
      iid.setId(patient.getId());
      r.setReference(iid.getId());
      a.setPatient(r);

      // Reaction: Manifestation and Severity
      AllergyIntoleranceReactionComponent re = new AllergyIntoleranceReactionComponent();
      // Manifestation
      CodeableConcept cMani = new CodeableConcept();
      cMani.addCoding().setSystem("http://snomed.info/sct").setCode(ManifestationSnomedCode)
          .setDisplay(ManifestationSnomedDisplay);
      cMani.setText(ManifestationSnomedDisplay);
      re.getManifestation().add(cMani);
      // Severity
      re.setSeverity(AllergyIntoleranceSeverity.fromCode(ManifestationSeverityCode));
      // Encode Resource in JSON syntax
      FhirContext ctx = FhirContext.forR4();
      IParser pr = ctx.newJsonParser();
      aux = pr.encodeResourceToString(a);

    }

    return aux;
  }

  public String L00_5_GetIPSLabResults(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {

    // Create a context
    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);

    // Create a client
    IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);

    String aux = "Error:Patient_Not_Found";
    Bundle bu = client.search().forResource((Patient.class))
        .and(Patient.IDENTIFIER.exactly().systemAndCode(IdentifierSystem, IdentifierValue))
        .returnBundle(Bundle.class).execute();

    for (Bundle.BundleEntryComponent entry : bu.getEntry()) {
      if (entry.getResource().fhirType().toString() == "Patient") {

        String FullId = entry.getFullUrl();
        IdType iid = new IdType();
        iid.setValue(FullId);
        String id;

        id = iid.getIdPart();

        aux = GetLabResults(client, id);
      }
      break;
    }
    return aux;
  }

  private String GetLabResults(IGenericClient client, String id) {

    String aux = "";
    String MySearch = "Bundle?composition.patient=" + id;
    Bundle bu = client.search()
        .byUrl(MySearch)
        .returnBundle(Bundle.class)
        .execute();

    aux = "Error:No_IPS";
    for (Bundle.BundleEntryComponent entry : bu.getEntry()) {
      Bundle OneDoc = (Bundle) entry.getResource();
      aux = "";
      for (Bundle.BundleEntryComponent entdo : OneDoc.getEntry()) {

        if (entdo.getResource().getResourceType().toString() == "Observation") {
          Observation oneP = (Observation) entdo.getResource();
          String m_category = oneP.getCategoryFirstRep().getCodingFirstRep().getCode().toLowerCase();
          if (m_category.equals("laboratory")) {
            // Must support for IPS Laboratory
            String m_code = "";
            CodeableConcept c = oneP.getCode();
            String u_code = c.getCodingFirstRep().getCode();
            //
            // This is null for the grouper
            // we only want the observations with results
            //
            if (u_code != null) {
              m_code = c.getCodingFirstRep().getCode() + ":" + c.getCodingFirstRep().getDisplay();

            }
            if (m_code != "") {
              String m_result = "";
              if (oneP.hasValueStringType()) {
                m_result = oneP.getValueStringType().toString();
              }
              if (oneP.hasValueQuantity()) {
                m_result = oneP.getValueQuantity().getValue().toPlainString() + " " + oneP.getValueQuantity().getUnit();
              }
              if (oneP.hasValueCodeableConcept()) {
                m_result = oneP.getValueCodeableConcept().getCodingFirstRep().getCode() + ":"
                    + oneP.getValueCodeableConcept().getCodingFirstRep().getDisplay();
              }

              String m_status = oneP.getStatus().toString();
              String m_datefo = oneP.getEffectiveDateTimeType().getValueAsString();

              aux += m_code + "|" + m_datefo + "|" + m_status + "|" + m_result + "\n";

            }
          }
        }
      }
      if (aux == "") {
        aux = "Error:IPS_No_Observations";
      }
    }

    return aux;
  }

  public String L00_6_ExpandValueset(
      String ServerEndPoint,
      String url,
      String filter)

  {
    // Create a context
    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
    String aux = "";
    try {
      // Create a client
      IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
      Parameters inP = new Parameters();
      inP.addParameter("url", new StringType(url));
      inP.addParameter("filter", new StringType(filter));
      Parameters outParams = client
          .operation()
          .onType(ValueSet.class)
          .named("expand")
          .withParameters(inP)
          .execute();

      ValueSet v = (ValueSet) outParams.getParameter().get(0).getResource();
      ValueSet.ValueSetExpansionComponent ve = v.getExpansion();
      for (int i = 0; i < ve.getTotal(); i++) {
        ValueSet.ValueSetExpansionContainsComponent ci = ve.getContains().get(i);
        String code = ci.getCode();
        String display = ci.getDisplay();
        aux = aux + code + "|" + display + "\n";
      }
    } catch (Exception ex) {
      aux = "Error:" + ex.getMessage();
    }

    if (aux == "") {
      aux = "Error:ValueSet_Filter_Not_Found";
    }
    return aux;

  }

}