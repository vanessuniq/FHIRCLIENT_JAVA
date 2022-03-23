package com.fhirintcourse.u2;

import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;

import ca.uhn.fhir.rest.client.api.IGenericClient;

public class L05_1_TerminologyService {

  public String ExpandValuesetForCombo(
      String ServerEndPoint,
      String url,
      String filter) {
    String aux = "";
    IGenericClient client = new L01_1_FetchDemographics().getClient(ServerEndPoint);
    Parameters params = new Parameters();
    params.addParameter("url", new StringType(url));
    params.addParameter("filter", new StringType(filter));
    Parameters outParams = client.operation()
        .onType(ValueSet.class)
        .named("expand")
        .withParameters(params)
        .execute();

    ValueSet v = (ValueSet) outParams.getParameter().get(0).getResource();
    ValueSetExpansionComponent vex = v.getExpansion();
    for (ValueSetExpansionContainsComponent vci : vex.getContains()) {
      String code = vci.getCode();
      String display = vci.getDisplay();
      aux += String.format("%s|%s%n", code, display);
    }
    if (aux.isEmpty())
      aux = "Error:ValueSet_Filter_Not_Found";
    return aux;

  }
}