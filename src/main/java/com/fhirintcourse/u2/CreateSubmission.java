package com.fhirintcourse.u2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.time.*;

import org.hl7.fhir.r4.model.TestReport.TestReportResult;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.TestReport;
import org.hl7.fhir.r4.model.TestReport.SetupActionAssertComponent;
import org.hl7.fhir.r4.model.TestReport.TestReportActionResult;
import org.hl7.fhir.r4.model.TestReport.TestReportParticipantComponent;
import org.hl7.fhir.r4.model.TestReport.TestReportParticipantType;
import org.hl7.fhir.r4.model.TestReport.TestReportStatus;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;


public class CreateSubmission {

    static private String L01_3_ObtainResult(String IdentifierValue) {
        String aux = "";
        L01_3_GetProvidersNearPatient L01_3 = new L01_3_GetProvidersNearPatient();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L01_3.GetProvidersNearCity(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }
        return aux;
    }

    static private String L01_1_ObtainResult(String IdentifierValue) {
        String aux = "";
        L01_1_FetchDemographics L01_1 = new L01_1_FetchDemographics();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L01_1.GetPatientPhoneAndEmail(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }
        return aux;
    }

    public static Hashtable<String, String> L01_3_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L01_3_T01", "L01_3_T01");
        my_tests.put("L01_3_T02", "L01_3_T02");
        my_tests.put("L01_3_T03", "L01_3_T03");
        my_tests.put("L01_3_T04", "L01_3_T04");
        my_tests.put("L01_3_T05", "L01_3_T05");

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String result = L01_3_ObtainResult(Patient);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static String L02_1_ObtainResult(String IdentifierValue) {
        String aux = "";
        L02_1_FetchEthnicity L02_1 = new L02_1_FetchEthnicity();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L02_1.GetEthnicity(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }
        return aux;
    }

    public static Hashtable<String, String> L02_1_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L02_1_T01", "L02_1_T01");
        my_tests.put("L02_1_T02", "L02_1_T02");
        my_tests.put("L02_1_T03", "L02_1_T03");
        my_tests.put("L02_1_T04", "L02_1_T04");
        my_tests.put("L02_1_T05", "L02_1_T05");
        
        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String result = L02_1_ObtainResult(Patient);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static class Immun {
        String identifierValue;
        String immunizationStatusCode;
        String immunizationDateTime;
        String productCVXCode;
        String productCVXDisplay;
        String reasonCode;

        public Immun(String IdentifierValue, String ImmunizationStatusCode, String ImmunizationDateTime,
                String ProductCVXCode, String ProductCVXDisplay, String ReasonCode

        ) {
            this.identifierValue = IdentifierValue;
            this.immunizationStatusCode = ImmunizationStatusCode;
            this.immunizationDateTime = ImmunizationDateTime;
            this.productCVXCode = ProductCVXCode;
            this.productCVXDisplay = ProductCVXDisplay;
            this.reasonCode = ReasonCode;
        }

    }

    public static class Person {
        String identifier;
        String family;
        String given;
        String gender;
        String birthDate;

        public Person(String Identifier, String Family, String Given, String Gender, String BirthDate) {
            this.identifier = Identifier;
            this.family = Family;
            this.given = Given;
            this.gender = Gender;
            this.birthDate = BirthDate;
        }

    }

    public static class Terms {
        String url;
        String filter;

        public Terms(String Url, String Filter) {
            this.url = Url;
            this.filter = Filter;
        }

    }

    public static class LabResult {
        String identifierValue;
        String observationStatusCode;
        String observationDateTime;
        String observationLOINCCode;
        String observationLOINCDisplay;
        String resultType;
        String numericResultValue;
        String numericResultUCUMUnit;
        String codedResultSNOMEDCode;
        String codedResultSNOMEDDisplay;

        public LabResult(String IdentifierValue, String ObservationStatusCode, String ObservationDateTime,
                String ObservationLOINCCode, String ObservationLOINCDisplay, String ResultType,
                String NumericResultValue, String NumericResultUCUMUnit, String CodedResultSNOMEDCode,
                String CodedResultSNOMEDDisplay) {
            this.identifierValue = IdentifierValue;
            this.observationStatusCode = ObservationStatusCode;
            this.observationDateTime = ObservationDateTime;
            this.observationLOINCCode = ObservationLOINCCode;
            this.observationLOINCDisplay = ObservationLOINCDisplay;
            this.resultType = ResultType;
            this.numericResultValue = NumericResultValue;
            this.numericResultUCUMUnit = NumericResultUCUMUnit;
            this.codedResultSNOMEDCode = CodedResultSNOMEDCode;
            this.codedResultSNOMEDCode = CodedResultSNOMEDDisplay;
        }

    }

    public static String L05_1_ObtainResult(Terms t)

    {
        String aux = "";
        L05_1_TerminologyService L05_1 = new L05_1_TerminologyService();
        Config c = new Config();
        String ServerEndPoint = c.TerminologyServerEndpoint();
        try {
            aux = L05_1.ExpandValuesetForCombo(ServerEndPoint, t.url, t.filter);

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static String L04_1_ObtainResult(LabResult l)

    {
        String aux = "";
        L04_1_CreateUSCoreObservation L04_1 = new L04_1_CreateUSCoreObservation();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        String IdentifierValue = l.identifierValue;
        String ObservationStatusCode = l.observationStatusCode;
        String ObservationDateTime = l.observationDateTime;
        String ObservationLOINCCode = l.observationLOINCCode;
        String ObservationLOINCDisplay = l.observationLOINCDisplay;
        String ResultType = l.resultType;
        String NumericResultValue = l.numericResultValue;
        String NumericResultUCUMUnit = l.numericResultUCUMUnit;
        String CodedResultSNOMEDCode = l.codedResultSNOMEDCode;
        String CodedResultSNOMEDDisplay = l.codedResultSNOMEDDisplay;
        try {
            aux = L04_1.CreateUSCoreR4LabObservation(ServerEndPoint, IdentifierSystem, IdentifierValue,
                    ObservationStatusCode, ObservationDateTime, ObservationLOINCCode, ObservationLOINCDisplay,
                    ResultType, NumericResultValue, NumericResultUCUMUnit, CodedResultSNOMEDCode,
                    CodedResultSNOMEDDisplay

            );

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static String L04_2_ObtainResult(Immun i)

    {
        String aux = "";
        L04_2_CreateUSCoreImmunization L04_2 = new L04_2_CreateUSCoreImmunization();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        String IdentifierValue = i.identifierValue;

        String ImmunizationStatusCode = i.immunizationStatusCode;
        String ImmunizationDateTime = i.immunizationDateTime;
        String ProductCVXCode = i.productCVXCode;
        String ProductCVXDisplay = i.productCVXDisplay;
        String ReasonCode = i.reasonCode;
        try {
            aux = L04_2.CreateUSCoreR4Immunization(ServerEndPoint, IdentifierSystem, IdentifierValue,
                    ImmunizationStatusCode, ImmunizationDateTime, ProductCVXCode, ProductCVXDisplay, ReasonCode);

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static Hashtable<String, String> L05_1_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();

        Hashtable<String, Terms> my_tests = new Hashtable<String, Terms>();
        my_tests.put("L05_1_T01", new Terms("http://snomed.info/sct?fhir_vs=isa/73211009", "diaxetes"));
        my_tests.put("L05_1_T02",
                new Terms("http://snomed.info/sct?fhir_vs=isa/73211009", "Drug-induced diabetes mellitus"));

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            Terms i = my_tests.get(str);
            String result = L05_1_ObtainResult(i);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static Hashtable<String, String> L04_2_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();

        Hashtable<String, Immun> my_tests = new Hashtable<String, Immun>();
        my_tests.put("L04_2_T01", new Immun("L04_2_T01", "", "", "", "", ""));
        my_tests.put("L04_2_T02", new Immun("L04_2_T02", "completed", "2021-10-25", "173", "", "cholera, BivWC"));
        my_tests.put("L04_2_T03", new Immun("L04_2_T02", "not-done", "2021-10-30T10:30:00Z", "207",
                "COVID-19, mRNA, LNP-S, PF, 100 mcg/0.5 mL dose", "IMMUNE"));

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            Immun i = my_tests.get(str);
            String result = L04_2_ObtainResult(i);
            if (str == "L04_2_T01") {
                my_results.put(str, result);
            } else {
                Config c = new Config();
                String valid="";
                if (valid!="")
                {
                    valid = ValidateImmunization(c.ServerEndpoint(), result);
                }
                else
                {
                    valid="";
                }
                my_results.put(str, valid);
            }
        }
        return my_results;
    }

    public static Hashtable<String, String> L04_1_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();

        Hashtable<String, LabResult> my_tests = new Hashtable<String, LabResult>();
        my_tests.put("L04_1_T01", new LabResult("L04_1_T01", "", "", "", "", "", "", "", "", ""));
        my_tests.put("L04_1_T02", new LabResult("L04_1_T02", "final", "2020-10-11T20:30:00Z", "5778-5",
                "Color or Urine", "coded", "", "", "371244009", "Yellow"));
        my_tests.put("L04_1_T03", new LabResult("L04_1_T02", "final", "2020-10-11T20:30:00Z", "1975-2",
                "Bilirubin, serum", "numeric", "8.6", "mg/dl", "", ""));

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            LabResult p = my_tests.get(str);
            String result = L04_1_ObtainResult(p);
            if (str == "L04_1_T01") {
                my_results.put(str, result);
            } else {
                String valid;
                Config c = new Config();
                if (result!="")
                {    valid = ValidateObservation(c.ServerEndpoint(), result);}
                else
                {   valid=""; }
                my_results.put(str, valid);
            }
        }
        return my_results;
    }

    private static String ValidateObservation(String ServerEndPoint, String resourceTextJson) {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
        IParser parser = ctx.newJsonParser();
        Observation ob = parser.parseResource(Observation.class, resourceTextJson);
        Parameters inParams = new Parameters();
        {
            inParams.addParameter().setName("resource").setResource(ob);
        }

        Parameters outParams = client.operation().onType(Observation.class).named("$validate").withParameters(inParams)
                .execute();

        OperationOutcome oo = (OperationOutcome) outParams.getParameter().get(0).getResource();
        String auxE = "OK";
        if (!(oo.getIssueFirstRep().getDetails().getText().equals("Validation successful, no issues found"))) {
            auxE = "Error:" + oo.getIssueFirstRep().getDetails().getText();
        }

        return auxE;
    }

    private static String ValidateImmunization(String ServerEndPoint, String resourceTextJson) {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
        IParser parser = ctx.newJsonParser();
        Immunization im = parser.parseResource(Immunization.class, resourceTextJson);
        Parameters inParams = new Parameters();
        {
            inParams.addParameter().setName("resource").setResource(im);
        }

        Parameters outParams = client.operation().onType(Immunization.class).named("$validate").withParameters(inParams)
                .execute();

        OperationOutcome oo = (OperationOutcome) outParams.getParameter().get(0).getResource();
        String auxE = "OK";
        if (!(oo.getIssueFirstRep().getDetails().getText().equals("Validation successful, no issues found"))) {
            auxE = "Error:" + oo.getIssueFirstRep().getDetails().getText();
        }

        return auxE;
    }

    public static String L01_2_ObtainResult(Person p) {

        String aux = "";
        L01_2_CompareDemographics L01_2 = new L01_2_CompareDemographics();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        String IdentifierValue = p.identifier;
        String myFamily = p.family;
        String myGiven = p.given;
        String myGender = p.gender;
        String myBirth = p.birthDate;
        try {
            aux = L01_2.GetDemographicComparison(ServerEndPoint, IdentifierSystem, IdentifierValue, myFamily, myGiven,
                    myGender, myBirth);

        } catch (Exception e) {
            aux = e.getMessage();

        }
        return aux;
    }

    public static Hashtable<String, String> L01_2_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();

        Hashtable<String, Person> my_tests = new Hashtable<String, Person>();
        my_tests.put("L01_2_T01", new Person("L01_2_T01", "", "", "", ""));
        my_tests.put("L01_2_T02", new Person("L01_2_T02", "Dougras", "Jamieson Harris", "male", "1968-07-23"));
        my_tests.put("L01_2_T03", new Person("L01_2_T02", "Douglas", "Jamieson", "male", "1968-07-23"));
        my_tests.put("L01_2_T04", new Person("L01_2_T02", "Douglas", "Jamieson Harris", "male", "1968-07-24"));
        my_tests.put("L01_2_T05", new Person("L01_2_T02", "Douglas", "Jamieson Harris", "female", "1968-07-23"));
        my_tests.put("L01_2_T06", new Person("L01_2_T02", "Douglas", "Jamieson Harris", "male", "1968-07-23"));

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            Person p = my_tests.get(str);
            String result = L01_2_ObtainResult(p);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static Hashtable<String, String> L01_1_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L01_1_T01", "L01_1_T01");
        my_tests.put("L01_1_T02", "L01_1_T02");
        my_tests.put("L01_1_T03", "L01_1_T03");
        my_tests.put("L01_1_T04", "L01_1_T04");
        my_tests.put("L01_1_T05", "L01_1_T05");
        my_tests.put("L01_1_T06", "L01_1_T06");

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String result = L01_1_ObtainResult(Patient);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static String L03_1_ObtainResult(String IdentifierValue) {
        String aux = "";
        L03_1_FetchImmunization L03_1 = new L03_1_FetchImmunization();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L03_1.GetImmunizations(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static String L03_3_ObtainResult(String IdentifierValue, String Class) {
        String aux = "";
        L03_3_FetchIPSClinicalData L03_3 = new L03_3_FetchIPSClinicalData();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            if (Class == "medications")
                aux = L03_3.GetIPSMedications(ServerEndPoint, IdentifierSystem, IdentifierValue);
            else
                aux = L03_3.GetIPSImmunizations(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static Hashtable<String, String> L03_3_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L03_3_T01", "L03_3_T01");
        my_tests.put("L03_3_T02", "L03_3_T02");
        my_tests.put("L03_3_T03", "L03_3_T03");
        my_tests.put("L03_3_T04", "L03_3_T04");
        my_tests.put("L03_3_T05", "L03_3_T04");
        my_tests.put("L03_3_T06", "L03_3_T03");

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String sType = "medications";
            if (str == "L03_3_T05") {
                sType = "immunizations";
            }
            if (str == "L03_3_T06") {
                sType = "immunizations";
            }

            String result = L03_3_ObtainResult(Patient, sType);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static Hashtable<String, String> L03_2_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L03_2_T01", "L03_2_T01");
        my_tests.put("L03_2_T02", "L03_2_T02");
        my_tests.put("L03_2_T03", "L03_2_T03");
        my_tests.put("L03_2_T04", "L03_2_T04");

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String result = L03_2_ObtainResult(Patient);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static String L03_2_ObtainResult(String IdentifierValue) {
        String aux = "";
        L03_2_FetchMedication L03_2 = new L03_2_FetchMedication();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        try {
            aux = L03_2.GetMedications(ServerEndPoint, IdentifierSystem, IdentifierValue);

        } catch (Exception e) {
            aux = e.getMessage();

        }

        return aux;
    }

    public static Hashtable<String, String> L03_1_RunTests() {
        Hashtable<String, String> my_results = new Hashtable<String, String>();
        Hashtable<String, String> my_tests = new Hashtable<String, String>();
        my_tests.put("L03_1_T01", "L03_1_T01");
        my_tests.put("L03_1_T02", "L03_1_T02");
        my_tests.put("L03_1_T03", "L03_1_T03");
        my_tests.put("L03_1_T04", "L03_1_T04");

        Set<String> keys = my_tests.keySet();

        // Obtaining iterator over set entries
        Iterator<String> itr = keys.iterator();

        // Displaying Key and value pairs
        while (itr.hasNext()) {
            String str = itr.next();
            String Patient = my_tests.get(str);
            String result = L03_1_ObtainResult(Patient);
            my_results.put(str, result);
        }
        return my_results;
    }

    public static Hashtable<String, String> AddResults(Hashtable<String, String> overall,
            Hashtable<String, String> added

    ) {
        overall.putAll(added);
        return overall;
    }

    public static Hashtable<String, String> RunAllTests() {
        Hashtable<String, String> overall_results = new Hashtable<String, String>();
        
        // L01_1 Tests
        Hashtable<String, String> L1_01_results = L01_1_RunTests();
        overall_results = AddResults(overall_results, L1_01_results);
        // L01_2 Tests
        Hashtable<String, String> L1_02_results = L01_2_RunTests();
        overall_results = AddResults(overall_results, L1_02_results);
        // L01_3 Tests
        Hashtable<String, String> L1_03_results = L01_3_RunTests();
        overall_results = AddResults(overall_results, L1_03_results);
        // L02_1 Tests
        Hashtable<String, String> L2_01_results = L02_1_RunTests();
        overall_results = AddResults(overall_results, L2_01_results);
        // L03_1 Tests
        Hashtable<String, String> L3_01_results = L03_1_RunTests();
        overall_results = AddResults(overall_results, L3_01_results);
        // L03_2 Tests
        Hashtable<String, String> L3_02_results = L03_2_RunTests();
        overall_results = AddResults(overall_results, L3_02_results);
        // L03_3 Tests
        Hashtable<String, String> L3_03_results = L03_3_RunTests();
        overall_results = AddResults(overall_results, L3_03_results);
        // L04_1 Tests
        Hashtable<String, String> L4_01_results = L04_1_RunTests();
        overall_results = AddResults(overall_results, L4_01_results);
        // L04_2 Tests
        Hashtable<String, String> L4_02_results = L04_2_RunTests();
        overall_results = AddResults(overall_results, L4_02_results);
        // L05_1 Tests
        Hashtable<String, String> L5_01_results = L05_1_RunTests();
        overall_results = AddResults(overall_results, L5_01_results);

        return overall_results;
    }

    public static void main(String[] args) {

        Hashtable<String, String> overall_results = RunAllTests();
        try {
            CreateTestReport(overall_results);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }

    private static void CreateTestReport(Hashtable<String, String> overall_results) {

        TestReport tr = new TestReport();
        Config c = new Config();
        tr.setResult(TestReportResult.PENDING);
        tr.setStatus(TestReportStatus.INPROGRESS);
        tr.getTestScript().setIdentifier(new Identifier().setSystem("http://fhirintermediate.org/test_script/id")
                .setValue("FHIR_INTERMEDIATE_U02-JAVA"));
        tr.setTester(c.StudentId());
        tr.setIssued(Date.from(Instant.now()));
        String datee=Instant.now().toString();
        tr.getIdentifier().setSystem("http://fhirintermediate.org/test_report/id").setValue(  c.StudentId() + "_"+datee);
        TestReportParticipantComponent trps = new TestReportParticipantComponent();
        trps.setDisplay("Resource Server");
        trps.setUri(c.ServerEndpoint());
        trps.setType(TestReportParticipantType.SERVER);
        tr.addParticipant(trps);

        TestReportParticipantComponent trpt = new TestReportParticipantComponent();
        trpt.setDisplay("Terminology Server");
        trpt.setUri(c.TerminologyServerEndpoint());
        trpt.setType(TestReportParticipantType.SERVER);
        tr.addParticipant(trpt);

        TestReportParticipantComponent trpc = new TestReportParticipantComponent();
        trpc.setDisplay(c.StudentName());
        trpc.setUri("http://localhost");

        trpc.setType(TestReportParticipantType.CLIENT);
        tr.addParticipant(trpc);

        List<String> tmp = Collections.list(overall_results.keys());
        Collections.sort(tmp);
        Iterator<String> itr = tmp.iterator();
        
        while (itr.hasNext()) {

            String str = itr.next();
            String result = overall_results.get(str);

          
            TestReport.TestReportTestComponent t = new TestReport.TestReportTestComponent();
            t.setDescription(str).setName(str).setId(str);

            SetupActionAssertComponent saac = new SetupActionAssertComponent();
            if (result!="") 
            {
                saac.setResult(TestReportActionResult.PASS);
                saac.setMessage(result);
            }
            else
            {
                saac.setResult(TestReportActionResult.FAIL);
                saac.setMessage("Not Attempted");
            }
            
            t.addAction().setAssert(saac);
            

            tr.addTest(t);
        }

        FhirContext ctx = FhirContext.forR4();

        IParser p = ctx.newJsonParser();
        
        
        String filename = "FHIR_INTERMEDIATE_U2_SUBMISSION_" + c.StudentId() + "_"+datee+".JSON";
        byte[] strToBytes = p.encodeResourceToString(tr).getBytes();
        try {
            FileOutputStream outputStream = new FileOutputStream(filename);
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            
        }
            
            

        }     
    

    }