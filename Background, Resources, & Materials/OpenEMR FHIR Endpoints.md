# OpenEMR FHIR Endpoints

__NOTE: Authentication not required.__

## Patient

### Retrieve All Patients
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/Patient

### Retrieve Single Patient by ID
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/Patient/{ID}

## Practitioner

### Retrieve All Practitioners
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/Practitioner

### Retrieve Single Practitioner by ID
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/Practitioner/{ID}

## MedicationStatement

### Retrieve All MedicationStatements
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/MedicationStatement

### Retrieve Single MedicationStatement by ID
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/MedicationStatement/{ID}

### Retrieve all MedicationStatements for Patient by Patient ID
GET http://bmi9.engr.uconn.edu:10093/Hackathon/UCONN-FHIR/MedicationStatement?patient={patientId}

### Code values
__Extension http://openemr.uconn.edu/prescriptions#active__
| Code | Value    |
| -----|----------|
| 1    | Active   |
| -1   | Inactive |




