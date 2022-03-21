package com.fhirintcourse.u2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

//
// This module is a test for our demo package in Java, created to understand different ways to query a FHIR Server
// and retrieve/create Patient Demographic Information and US Core/IPS conformant resources
// These tests were created 
// 1. as a sanity check (something known is working)
// 2. So you can see how passed tests look alike
// 3. So you know how we test and how the tests are derived from the specs
// We will test:
// 1) The demo's ability to get a Patient's full name and address
// 2) How to get a US Core Race extension
// 3) How to read all US Core Condition resources for a patient
// 4) How to create a US Core Allergy conformant resource
// 5) How to read lab results from an IPS document for a patient
// 6) How to expand  a valueset using a terminology server
//
// These demo tests are less specific than our tests for the assignment, so you will
// see less tests per function here. They are just 'demo tests'
// Important: You are not required to write or modify any test
// Just write the functions you are required to write
//
@DisplayName("Tests for U2-L00_0:Demo Tests")

class L00_0_Tests {

    @Test
    @DisplayName("L00_1_T01:Patient Not Found")
    void L00_0_T01() {
        String IdentifierValue = "L00_1_T01";
        String ExpectedResult = "Error:Patient_Not_Found";
        String aux = "";
        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        aux = L00_1.L00_1_GetPatientFullNameAndAddress(ServerEndPoint, IdentifierSystem, IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(), aux.toUpperCase());

    };

    @Test
    @DisplayName("L00_1_T02:Patient With Full Name and Two Addresses")

    void L00_0_T02() {
        String IdentifierValue = "L00_1_T02";
        String aux = "";
        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        aux = L00_1.L00_1_GetPatientFullNameAndAddress(ServerEndPoint, IdentifierSystem, IdentifierValue);
        // Spec
        // GetPatientFullNameAndAddress shall return a string with the Full Name of the
        // patient
        // and all the addresses.
        // Format:
        // Full Name:{family},{given}\n
        // (for each patient's address)
        // [Address:{line} - {city} , {state} , {country} (postalCode) / ]\n
        // Message if patient not found: Error:Patient_Not_Found
        //
        String ExpectedResult = "FULL NAME:PATIENT L00_1_T02 FAM,PATIENT L00_1_T02 GIV\n";
        ExpectedResult += "ADDRESS:128 THIS PATIENT DRIVE SUITE 318  - ANN ARBOR , MI , US (48103) / 256 THIS PATIENT AVE SUITE 320 - PENT HOUSE  - MONROE , MI , US (48161) / \n";
        assertEquals(ExpectedResult.toUpperCase(), aux.toUpperCase());

    };

    @Test
    @DisplayName("L00_1_T03:Patient With a Complete US-Core Race Conformant Extension")
    void L00_0_T03() {
        String IdentifierValue = "L00_1_T03";
        String aux = "";
        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        String ExpectedResult = "TEXT|MIXED\nCODE|2028-9:ASIAN\nDETAIL|1586-7:SHOSHONE\nDETAIL|2036-2:FILIPINO\n";
        // Spec
        // GetUSCoreRace shall return a string with the contents of the US-Core race
        // extension
        // Format:
        // text|{text sub extension}\n (mandatory)
        // code|{coded sub extension}\n (mandatory)
        // detail|{detailed sub extensions}\n (may repeat)
        // Message if patient not found: Error:Patient_Not_Found
        // Message if extension not found: see implementation
        // Message if extension not conformant: see implementation
        //
        aux = L00_1.L00_2_GetUSCoreRace(ServerEndPoint, IdentifierSystem, IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(), aux.toUpperCase());

    };

    @Test
    @DisplayName("L00_1_T04:Patient With Several US Core Conditions")
    void L00_0_T04() {
        String IdentifierValue = "L00_1_T04";
        String aux = "";
        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        // Spec
        // GetConditions shall return a string with the contents of all patient's US
        // CORE Conditions
        // containing mandatory elements, as follows:
        // Format: for each condition:
        // {ClinicalStatus}|{VerificationStatus}|{Category}|{code}:{display}|{date}\n
        // Message if patient not found: Error:Patient_Not_Found
        // Message if no conditions found: see implementation
        //
        String ExpectedResult = "ACTIVE|CONFIRMED|PROBLEM LIST ITEM|442311008:LIVEBORN BORN IN HOSPITAL|2020-08-10\nACTIVE|CONFIRMED|PROBLEM LIST ITEM|69347004:NEONATAL JAUNDICE (DISORDER)|2020-08-10\n";
        aux = L00_1.L00_3_GetConditions(ServerEndPoint, IdentifierSystem, IdentifierValue);
        assertEquals(ExpectedResult.toUpperCase(), aux.toUpperCase());

    };

    @Test
    @DisplayName("L00_1_T05:Create US Core Conformant Allergy Resource")
    void L00_0_T05() {
        String IdentifierValue = "L00_1_T04";
        String aux = "";
        String ClinicalStatusCode = "active";
        String VerificationStatusCode = "confirmed";
        String CategoryCode = "medication";
        String CriticalityCode = "high";
        String AllergySnomedCode = "387406002";
        String AllergySnomedDisplay = "Sulfonamide (substance)";
        String ManifestationSnomedCode = "271807003";
        String ManifestationSnomedDisplay = "Skin Rash";
        String ManifestationSeverityCode = "mild";

        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        aux = L00_1.L00_4_CreateUSCoreAllergyIntolerance(ServerEndPoint, IdentifierSystem, IdentifierValue,
                ClinicalStatusCode, VerificationStatusCode, CategoryCode, CriticalityCode, AllergySnomedCode,
                AllergySnomedDisplay, ManifestationSnomedCode, ManifestationSnomedDisplay, ManifestationSeverityCode);
        // Spec
        // CreateUSCoreAllergyIntolerance shall return a string with a JSON FHIR
        // Resource
        // conformant with the US Core AllergyIntolerance profile
        // or Message if patient not found: Error:Patient_Not_Found
        //

        String val = ValidateResource(c.ServerEndpoint(), aux);
        assertEquals(val, "OK");

    }

    @Test
    @DisplayName("L00_1_T06:Terminology : Term Found")

    void L00_1_T06() {

        Config c = new Config();
        String TermServer = c.TerminologyServerEndpoint();
        String ExpectedResult = "64432007|RADIOISOTOPE MYOCARDIAL IMAGING PROCEDURE\n";
        String url = "http://snomed.info/sct?fhir_vs";
        String filter = "Radioisotope myocardial imaging procedure";
        L00_0_Demo L00_1 = new L00_0_Demo();
        // Spec
        // ExpandValueset shall return a string with one record for each concept found
        // in the expansion
        // {code}|{display}
        // or Message if term not found: Error:Term/Concept not found
        //
        String result = L00_1.L00_6_ExpandValueset(TermServer, url, filter);
        assertEquals(ExpectedResult.toUpperCase(), result.toUpperCase());

    }

    @Test
    @DisplayName("L00_1_T07:Get Lab Results from IPS Document")

    void L00_1_T07() {

        String IdentifierValue = "L03_3_T03";
        String aux = "";
        L00_0_Demo L00_1 = new L00_0_Demo();
        Config c = new Config();
        String ServerEndPoint = c.ServerEndpoint();
        String IdentifierSystem = c.PatientIdentifierSystem();
        String match = "882-1:ABO and Rh group [Type] in Blood|2015-10-10T09:15:00+01:00|final|278149003:Blood group A Rh(D) positive\n";
        match += "945-6:C Ab [Presence] in Serum or Plasma|2015-10-10T09:35:00+01:00|final|10828004:Positive\n";
        match += "1018-1:E Ab [Presence] in Serum or Plasma|2015-10-10T09:35:00+01:00|final|10828004:Positive\n";
        match += "1156-9:little c Ab [Presence] in Serum or Plasma|2015-10-10T09:35:00+01:00|final|260385009:Negative\n";
        match += "17856-6:Hemoglobin A1c/Hemoglobin.total in Blood by HPLC|2017-11-10T08:20:00+01:00|final|7.5 %\n";
        match += "42803-7:Bacteria identified in Isolate|2017-12-10T08:20:00+01:00|final|115329001:Methicillin resistant Staphylococcus aureus\n";
        // Spec
        // GetIPSLabResuls shall search an IPS document based on the patient's internal
        // id
        // and then return a string with one record for each IPS lab result Observation
        // with the format
        // {code}:{display}|{effectiveDateTime}|{status}|{result}\n
        // {result} maybe {quantity units} or {code:display} or {string} depending on
        // the result type

        aux = L00_1.L00_5_GetIPSLabResults(ServerEndPoint, IdentifierSystem, IdentifierValue);
        assertEquals(match.toUpperCase(), aux.toUpperCase());

    }

    private String ValidateResource(String ServerEndPoint, String resourceTextJson) {
        FhirContext ctx = FhirContext.forR4();
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
        IGenericClient client = ctx.newRestfulGenericClient(ServerEndPoint);
        IParser parser = ctx.newJsonParser();
        AllergyIntolerance ob = parser.parseResource(AllergyIntolerance.class, resourceTextJson);
        Parameters inParams = new Parameters();
        {
            inParams.addParameter().setName("resource").setResource(ob);
        }

        Parameters outParams = client.operation().onType(AllergyIntolerance.class).named("$validate")
                .withParameters(inParams).execute();

        OperationOutcome oo = (OperationOutcome) outParams.getParameter().get(0).getResource();
        String auxE = "OK";
        if (!(oo.getIssueFirstRep().getDetails().getText().equals("Validation successful, no issues found"))) {
            auxE = "Error:" + oo.getIssueFirstRep().getDetails().getText();
        }

        return auxE;
    }

}
