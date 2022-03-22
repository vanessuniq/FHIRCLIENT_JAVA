package com.fhirintcourse.u2;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;

public class L02_1_FetchEthnicity {

  public String GetEthnicity(String ServerEndPoint, String IdentifierSystem, String IdentifierValue)

  {
    String aux = "";
    BundleEntryComponent ptEntry = new L01_1_FetchDemographics().getPatient(ServerEndPoint, IdentifierSystem,
        IdentifierValue);
    Patient pt = ptEntry == null ? null : (Patient) ptEntry.getResource();
    if (pt != null) {
      String ethnicityExtensionUrl = "http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity";
      if (pt.hasExtension(ethnicityExtensionUrl)) {
        Extension ethnicityExtension = pt.getExtensionByUrl(ethnicityExtensionUrl);
        aux = getUSCoreEthnicityExtension(ethnicityExtension);
      } else {
        aux = "Error:No_us-core-ethnicity_Extension";
      }
    } else {
      aux = "Error:Patient_Not_Found";
    }
    return aux;
  }

  private String getUSCoreEthnicityExtension(Extension ethnicityExtension) {
    String ombCategory = "";
    String detailed = "";
    String text = "";
    String aux = "";
    if (ethnicityExtension.hasExtension()) {
      for (Extension extension : ethnicityExtension.getExtension()) {
        String url = extension.getUrl();
        if (url.equals("text")) {
          text += String.format("text|%s%n", extension.getValue());
        } else if (url.equals("ombCategory")) {
          Coding c = (Coding) extension.getValue();
          ombCategory += String.format("code|%s:%s%n", c.getCode(), c.getDisplay());
        } else if (url.equals("detailed")) {
          Coding coding = (Coding) extension.getValue();
          detailed += String.format("detail|%s:%s%n", coding.getCode(), coding.getDisplay());
        }
      }
      aux += text + ombCategory + detailed;
    }
    if (ombCategory.isEmpty() || text.isEmpty())
      aux = "Error:Non_Conformant_us-core-ethnicity_Extension";
    return aux;
  }

}