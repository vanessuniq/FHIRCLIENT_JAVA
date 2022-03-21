package com.fhirintcourse.u2;

public class Config
 {


         public String ServerEndpoint()
         {
          return  "http://wildfhir4.aegis.net/fhir4-0-1";
         }

        public String PatientIdentifierSystem()
        {
          return "http://fhirintermediate.org/patient_id";
        }

        public String TerminologyServerEndpoint()
        {
            return "https://r4.ontoserver.csiro.au/fhir";
        }
        public String AssignmentSubmissionFHIRServer()
        {return
              "http://fhirserver.hl7fundamentals.org/fhir/";
            }

        public String StudentId()
        {
             return "vfotso@mitre.org";
            }
        public String StudentName()
        {return
            "Vanessa Fotso";
        }

}
