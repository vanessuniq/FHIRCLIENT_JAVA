package com.fhirintcourse.u2;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

public class L01_1_FetchDemographics {

  public String GetPatientPhoneAndEmail(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {
    BundleEntryComponent ptEntry = getPatient(ServerEndPoint, IdentifierSystem, IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();

    String aux = "";
    if (pt != null) {
      String emails = "Emails:-";
      String phones = "Phones:-";
      for (ContactPoint contact : pt.getTelecom()) {
        if (contact.hasSystem() && contact.getSystem().toCode().equals("email")) {
          emails = emails.endsWith("-") ? StringUtils.chop(emails) : emails;
          emails += contact.getValue() + "(" + contact.getUse().toString() + ")" + ",";
        } else if (contact.hasSystem() && contact.getSystem().toCode().equals("phone")) {
          phones = phones.endsWith("-") ? StringUtils.chop(phones) : phones;
          phones += contact.getValue() + "(" + contact.getUse().toString() + ")" + ",";
        }
      }
      emails = emails.endsWith(",") ? StringUtils.chop(emails) + "\n" : emails + "\n";
      phones = phones.endsWith(",") ? StringUtils.chop(phones) + "\n" : phones + "\n";
      aux = emails + phones;
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

  public BundleEntryComponent getPatient(String server, String patientIdentifierSystem, String patientIdentifierValue) {
    IGenericClient client = getClient(server);
    BundleEntryComponent pt = null;
    Bundle bundle = client.search()
        .forResource(Patient.class)
        .and(Patient.IDENTIFIER.exactly()
            .systemAndCode(patientIdentifierSystem, patientIdentifierValue))
        .returnBundle(Bundle.class).execute();
    if (bundle.hasEntry() && !bundle.getEntry().isEmpty())
      pt = bundle.getEntry().get(0);
    return pt;
  }

  public IGenericClient getClient(String server) {
    // Create context
    FhirContext ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
    ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
    // Client
    return ctx.newRestfulGenericClient(server);
  }

}
