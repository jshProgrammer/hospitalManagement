# Persons
Base URL: http://localhost:8080

## Sections
- [Patients](#patients)
    - [GET](#get)
    - [POST](#post)
- [Nurses](#nurses)
    - [GET](#get-1)
    - - [POST](#post-1)
- [Doctors](#doctors)
    - [GET](#get-2)
    - [POST](#post-2)


## Patients
- **Endpoint**: `/api/patients`

### GET

- `GET /api/patients?page=0&size=10`: get all patients with pagination
- `GET /api/patients/{patient_id}`: get patient with specific id
- `GET /api/patients/{firstName}/{lastName}`: get all patients with first and last name
- `GET /api/{patient_id}/bookings")`: get all bookings per patient id

Please note that a `404 NOT FOUND` error will be returned if the patient with the specified ID/variables does not exist.


Example response for `GET /api/patients/1`:
```json
{
  "id": 1,
  "person": {
    "id": "0d39c174-968c-4b38-ba09-72f7bab732c7",
    "gender": "f",
    "firstName": "Cassie",
    "lastName": "Pella",
    "plz": 13680,
    "city": "North Michelefort",
    "street": "Macias Camp",
    "streetNo": 769,
    "country": "USA",
    "birthday": "1980-05-06",
    "phone": "015798878086",
    "email": "sara1998@example.com"
  }
}
```


### POST

#### Creating new Patient based on existing Person
- **Endpoint**: `POST /api/patients/new/0d39c174-968c-4b38-ba09-72f7bab732c7`

Resulting in the following response:
```json
{
  "id": 1,
  "personId": "0d39c174-968c-4b38-ba09-72f7bab732c7"
}
```

### Creating new Patient with search
- **Endpoint**: `POST /api/patients/new`
Request body:
```json
{
  "gender": "m",
  "firstName": "Hans",
  "lastName": "Müller",
  "email": "hans.mueller@example.com",
  "phoneNumber": "49 176 12345678",
  "plz": 97070,
  "city": "Würzburg",
  "street": "Domstraße",
  "houseNumber": "5",
  "country": "Deutschland",
  "birthday": "1985-03-15"
}
```
Case 1: 
If a person with the same email, first- and lastname already exists, afterwards we have to take the personId and create
the patient based on this personId.  
Response:
```json
{
"potentialMatches": [
{
"id": "0d39c174-968c-4b38-ba09-72f7bab732c7",
"firstName": "Max",
"lastName": "Mustermann",
"birthday": "1990-01-01",
"isEmployee": false,
"isPatient": false
}
],
"createdPatient": null
}
```
Case 2: 
If no person exists, a new person will be created and assigned to the new patient. 
Response: 
```json
{
  "potentialMatches": null,
  "createdPatient": {
    "id": 1,
    "personId": "0d39c174-968c-4b38-ba09-72f7bab732c7"
  }
}
```

### Relocating patient
- **Endpoint**: `POST /api/patients/{PATIENT_ID}/relocate`

In the header section:
```json
{
  "room_id": 1
}
```
This action will set the `until` attribute of the booking with the old room and the `state` to `RELOCATED`.
A new booking entry that is being returned will be added with the new `room_id` and a `CONFIRMED`state.


### Discharging patient
- **Endpoint**: `POST /api/patients/{PATIENT_ID}/discharge`

This action will set the `until` attribute of the newest booking that is either `confirmed` or `checked_in`to the current date.
Besides, it will set the `state` to `COMPLETED`.

- - -

## Nurses
- **Endpoint**: `/api/nurses`


### GET
- `GET /api/nurses{id}`: get Nurse based on ID
- `GET /api/nurses/station/{stationId}`: get all nurses based on station id
- `GET /api/nurses?page=0&size=10`: get all nurses with pagination

### POST 

Calls are the same as for doctors, but with different endpoints and without work-phone: 
- **Endpoint**: `POST /api/nurses/new/0d39c174-968c-4b38-ba09-72f7bab732c7`
- **Endpoint**: `POST /api/patients/new`
- - -

## Doctors
- **Endpoint**: `/api/doctors`

### GET
- `GET /api/doctors/{Id}`: get doctor based on ID
- `GET /api/doctors?page=0&size=10`: get all doctors with pagination
- `GET /api/docotors/type/{type}`: get Docotors based on type


Example response for `GET /api/doctors/b354c383-615e-4b05-afc2-4aba9b2d2174`:
```json
{
  "id": "b354c383-615e-4b05-afc2-4aba9b2d2174",
  "employee": {
    "id": "b354c383-615e-4b05-afc2-4aba9b2d2174",
    "person": {
      "id": "db983f74-90a4-4b9d-9660-5d355599118e",
      "gender": "f",
      "firstName": "Jerry",
      "lastName": "Murphy",
      "plz": 62922,
      "city": "New Sydney",
      "street": "Camacho Summit",
      "streetNo": 2089,
      "country": "USA",
      "birthday": "1986-02-23",
      "phone": "017076330101",
      "email": "christian.patton@example.net"
    },
    "department": 1
  },
  "work_phone": "49931201043321",
  "type": "HEAD_OF_DEPARTMENT"
}
```

### POST
Calls are again the same as for patients, but again with different endpoints: 
- **Endpoint**: `POST /api/doctors/new/0d39c174-968c-4b38-ba09-72f7bab732c7`
- **Endpoint**: `POST /api/doctors/new`

Example Creation Call (needs a bit more beautyfying, but you get the idea):
 POST http://localhost:8080/api/doctors/new
Request body
```json
{
  "gender": "m",
  "firstName": "Max",
  "lastName": "Mustermann",
  "email": "max.mustermann@example.com",
  "phoneNumber": "+49 176 12345678",
  "plz": 97070,
  "city": "Würzburg",
  "street": "Domstraße",
  "houseNumber": "5",
  "country": "Deutschland",
  "birthday": "1985-03-15", 
  "department": "1", 
  "workPhone": "+49 931 123456",
  "doctorType": "ATTENDING_PHYSICIAN"
}
```

Response:
```json
{
    "potentialMatches": null,
    "createdDoctor": {
        "id": "f5b63269-6c61-41ed-a489-6b57b4de9291",
        "employeeId": "f5b63269-6c61-41ed-a489-6b57b4de9291",
        "personId": "53442e41-79a5-4d9a-9088-02dbc24f90c0",
        "workPhone": " 49 931 123456",
        "type": "ATTENDING_PHYSICIAN",
        "department": 1
    }
}
```