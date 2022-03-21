package com.fhirintcourse.u2;

import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class L01_3_GetProvidersNearPatient {

  public String GetProvidersNearCity(String ServerEndPoint, String IdentifierSystem, String IdentifierValue) {

    String aux = "";
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    if (pt != null) {
      if (!pt.hasAddress() || !pt.getAddressFirstRep().hasCity()) {
        aux = "Error:Patient_w/o_City";
      } else {
        String city = pt.getAddressFirstRep().getCity();
        List<BundleEntryComponent> providers = getProviders(ServerEndPoint, city);
        if (providers.isEmpty()) {
          aux = "Error:No_Provider_In_Patient_City";
        } else {
          for (BundleEntryComponent entry : providers) {
            Practitioner provider = (Practitioner) entry.getResource();
            String family = provider.getNameFirstRep().getFamily();
            String given = provider.getNameFirstRep().getGivenAsSingleString();
            String system = provider.getTelecomFirstRep().getSystem().getDisplay();
            String phone = provider.getTelecomFirstRep().getValue();
            String line = provider.getAddressFirstRep().getLine().get(0).asStringValue();
            String role = provider.getQualificationFirstRep().getCode().getCodingFirstRep().getDisplay();
            aux += String.format("%s,%s|%s:%s|%s|%s%n", family, given, system, phone, line, role);
          }
        }
      }

    } else {
      aux = "Error:Patient_Not_Found";
    }

    return aux;
  }

  public List<BundleEntryComponent> getProviders(String server, String city) {
    IGenericClient client = new L01_1_FetchDemographics().getClient(server);
    String searchParam = "Practitioner?address-city=" + city;
    return client.search()
        .byUrl(searchParam)
        .returnBundle(Bundle.class)
        .execute()
        .getEntry();
  }

}
