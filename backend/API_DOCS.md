# Hospital Management Backend API

Base URL when running locally: `http://localhost:8080`

All collection endpoints that accept `Pageable` also accept Spring pagination parameters such as `page`, `size`, and `sort`.

Date values are accepted as ISO date strings unless noted otherwise(Datetime used in Medication, Diagnosis and Booking), for example `2024-03-01`.

## Sections
- [Enviroment](#environment)
- [Enums](#enum-values)
- [Facilities](#facilities)
  - [Departments](#departments)
  - [Stations](#stations)
  - [Rooms](#rooms)
  - [Bookings](#bookings)
- [Persons](#persons)
  - [Patients](#patients)
  - [Doctors](#doctors)
  - [Nurses](#nurses)
- [Medication-Domain](#medication-domain)
  - [Drugs](#drugs)
  - [Doses](#doses)
  - [Medications](#medications)
  - [Diagnoses](#diagnoses)
- [Common-Responses](#common-responses)


## Environment

The application reads database connection values from `.env` or environment variables:

```properties
DATABASE_URL=jdbc:postgresql://host:5432/database
DATABASE_USER=username
DATABASE_PASSWORD=password
```

## Enum Values

`BookingState`: `PENDING`, `CONFIRMED`, `CHECKED_IN`, `COMPLETED`, `CANCELLED`, `RELOCATED`, `NO_SHOW`, `CHECKED_OUT_EARLY`

`Gender`: `m`, `f`, `d`

`DoctorsType`: `ASSISTANT_PHYSICIAN`, `SENIOR_PHYSICIAN`, `CHIEF_PHYSICIAN`, `CONSULTANT`, `RESIDENT`, `ATTENDING_PHYSICIAN`, `HEAD_OF_DEPARTMENT`

`DoseUnit`: `MG`, `G`, `MCG`, `ML`, `L`, `TABLET`, `CAPSULE`, `DROP`, `PUFF`, `UNIT`

`DoseFrequency`: `EVERY_X_DAYS`, `X_DAILY`, `EVERY_X_HOURS`, `X_WEEKLY`, `EVERY_X_WEEKS`

`DrugsType`: `TABLET`, `CAPSULE`, `SYRUP`, `INJECTION`, `INFUSION`, `OINTMENT`, `CREAM`, `DROPS`, `SPRAY`, `SUPPOSITORY`

## Facilities

### Departments

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/departments` | List departments. |
| `GET` | `/api/departments/{id}` | Get one department by id. |

`GET /api/departments` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `name` | string | no | Exact department name. |
| `nameContains` | string | no | Case-insensitive name search. |
| `building` | string | no | Exact building. |

### Stations

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/stations` | List stations. |
| `GET` | `/api/stations/{id}` | Get one station by id. |

`GET /api/stations` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `name` | string | no | Exact station name. |
| `nameContains` | string | no | Case-insensitive name search. |
| `departmentId` | long | no | Filter by department id. |

### Rooms

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/rooms` | List rooms. |
| `GET` | `/api/rooms/{id}` | Get one room by id. |
| `GET` | `/api/rooms/floor/{floor}` | List rooms on a floor. |
| `GET` | `/api/rooms/{id}/bookings` | List bookings for a room. |

`GET /api/rooms` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `stationID` | long | no | Filter by station id. |
| `number` | long | no | Filter by room number. |

### Bookings

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/bookings` | List bookings. |
| `GET` | `/api/bookings/{id}` | Get one booking by id. |
| `POST` | `/api/bookings` | Create a booking. |

`GET /api/bookings` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `state` | `BookingState` | no | Filter by booking state. |

`POST /api/bookings` request body (`BookingRequest` Object):
(until is optional)

```json
{
  "from": "2024-03-01",
  "until": "2024-03-05", 
  "state": "CONFIRMED",
  "room_id": 1,
  "patient_id": 1
}
```

## Persons

### Patients

| Method | Endpoint | Description | Pageable |
| --- | --- | --- |----------|
| `POST` | `/api/patients/new` | Create a patient from new person data, or return potential duplicate person matches. | -        |
| `POST` | `/api/patients/new/{personId}` | Create a patient from an existing person UUID. | -        |
| `GET` | `/api/patients` | List/search patients. | Yes      |
| `GET` | `/api/patients/{id}` | Get one patient by patient id. | -        |
| `GET` | `/api/patients/{id}/bookings` | List bookings for a patient. | Yes      |
| `POST` | `/api/patients/{id}/discharge` | Discharge a patient from the current booking. | -        |
| `POST` | `/api/patients/{id}/relocate` | Relocate a patient to another room. | -        |
| `GET` | `/api/patients/{id}/diagnoses` | List diagnoses for a patient. | Yes      |

`GET /api/patients` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | date | no | Exact birthday. |
| `plz` | int | no | Postal code. |
| `street` | string | no | Exact street. |
| `streetNo` | int | no | House number. |

`POST /api/patients/new` request body (`PersonCreateRequest` Object):

```json
{
  "gender": "f",
  "firstName": "Lacey",
  "lastName": "Pellum",
  "email": "lacey.pellum@example.invalid",
  "phoneNumber": "015784699114",
  "plz": 97070,
  "city": "Wuerzburg",
  "street": "Example Street",
  "houseNumber": "12",
  "country": "Germany",
  "birthday": "1945-07-16"
}
```

`POST /api/patients/{id}/relocate` request body (`RelocateRequest` Object):

```json
{
  "room_id": 2
}
```

### Doctors

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/doctors/new` | Create a doctor from new person and employee data, or return potential duplicate person matches. |
| `POST` | `/api/doctors/new/{personId}` | Create a doctor from an existing person UUID and employee data. |
| `GET` | `/api/doctors` | List/search doctors. |
| `GET` | `/api/doctors/{id}` | Get one doctor by UUID. |

`GET /api/doctors` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | date | no | Exact birthday. |
| `plz` | int | no | Postal code. |
| `street` | string | no | Exact street. |
| `streetNo` | int | no | House number. |
| `type` | `DoctorsType` | no | Filter by doctor type. |
| `departmentId` | long | no | Filter by department id. |
| `workPhone` | string | no | Exact work phone. |

`POST /api/doctors/new` request body (`DoctorCreationRequest` Object):

```json
{
  "gender": "m",
  "firstName": "Alex",
  "lastName": "Morgan",
  "email": "alex.morgan@example.invalid",
  "phoneNumber": "015123456789",
  "plz": 97070,
  "city": "Wuerzburg",
  "street": "Clinic Road",
  "houseNumber": "8",
  "country": "Germany",
  "birthday": "1981-02-20",
  "department": 1,
  "workPhone": "+49-931-0001",
  "doctorType": "ATTENDING_PHYSICIAN"
}
```

`POST /api/doctors/new/{personId}` request body (`EmployeeCreationRequest` Object):

```json
{
  "department": 1,
  "workPhone": "+49-931-0001",
  "doctorType": "ATTENDING_PHYSICIAN",
  "stationId": null
}
```

### Nurses

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/nurses/new` | Create a nurse from new person and employee data, or return potential duplicate person matches. |
| `POST` | `/api/nurses/new/{personId}` | Create a nurse from an existing person UUID and employee data. |
| `GET` | `/api/nurses` | List/search nurses. |
| `GET` | `/api/nurses/{id}` | Get one nurse by UUID. |
| `GET` | `/api/nurses/station/{stationId}` | List nurses for one station. |

`GET /api/nurses` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | date | no | Exact birthday. |
| `plz` | int | no | Postal code. |
| `street` | string | no | Exact street. |
| `streetNo` | int | no | House number. |
| `stationId` | int | no | Filter by station id. |
| `departmentId` | long | no | Filter by department id. |

`POST /api/nurses/new` request body (`NurseCreationRequest` Object):

```json
{
  "gender": "f",
  "firstName": "Jamie",
  "lastName": "Reed",
  "email": "jamie.reed@example.invalid",
  "phoneNumber": "015987654321",
  "plz": 97070,
  "city": "Wuerzburg",
  "street": "Clinic Road",
  "houseNumber": "9",
  "country": "Germany",
  "birthday": "1990-06-11",
  "department": 1,
  "stationId": 1
}
```

`POST /api/nurses/new/{personId}` request body (`EmployeeCreationRequest` Object):

```json
{
  "department": 1,
  "workPhone": null,
  "doctorType": null,
  "stationId": 1
}
```

## Medication Domain

### Drugs

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/drugs` | List/search drugs. |
| `GET` | `/api/drugs/{id}` | Get one drug by id. |

`GET /api/drugs` query parameters:

| Name | Type | Required | Description                                                                                                 |
| --- | --- | --- |-------------------------------------------------------------------------------------------------------------|
| `name` | string | no | Exact drug name.                                                                                            |
| `nameContains` | string | no | Case-insensitive drug name search.                                                                          |
| `activeIngredient` | string | no | Exact active ingredient.                                                                                    |
| `type` | string | no | Drug type database value, for example `tablet` or `injection`.                                              |
| `criticalAmountInDays` | int | no | Filter by stock threshold logic in the service. Is not taken into consideration. Is not included in DB yet. |

### Doses

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/doses` | List/search doses. |
| `GET` | `/api/doses/{id}` | Get one dose by id. |

`GET /api/doses` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `unit` | `DoseUnit` | no | Filter by dose unit. |
| `frequency` | `DoseFrequency` | no | Filter by frequency. |
| `amount` | long | no | Filter by amount. |
| `frequencyAmount` | long | no | Filter by frequency amount. |

### Medications

| Method | Endpoint | Description |
|--------| --- | --- |
| `GET`  | `/api/medications` | List/search medications. |
| `GET`  | `/api/medications/{id}` | Get one medication by id. |
| `POST` | `/api/medications` | Create a medication. |
| `POST`   | `/api/medications/{id}` | Update a medication. |

`GET /api/medications` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `drugId` | long | no | Filter by drug id. |
| `drugType` | `DrugsType` | no | Filter by drug type. |
| `doseUnit` | `DoseUnit` | no | Filter by dose unit. |
| `startedAfter` | date | no | Include medications started on or after this date. |
| `startedBefore` | date | no | Include medications started on or before this date. |
| `active` | boolean | no | `true` for no end date, `false` for ended medications. |

`POST /api/medications` and `POST /api/medications/{id}` request body (`MedicationRequest` Object):

```json
{
  "dose_id": 1,
  "drug_id": 1,
  "started": "2024-03-01",
  "ended": null
}
```

### Diagnoses

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/diagnoses` | List/search diagnoses. |
| `GET` | `/api/diagnoses/{id}` | Get one diagnosis by id. |
| `POST` | `/api/diagnoses` | Create a diagnosis. |
| `POST` | `/api/diagnoses/{id}` | Update a diagnosis. |
| `POST` | `/api/diagnoses/{id}/terminate` | Set `diagnosed_end` to the backend's current date. |

`GET /api/diagnoses` query parameters:

| Name | Type        | Required | Description |
| --- |-------------| --- | --- |
| `disease` | string      | no | Exact disease. |
| `diseaseContains` | string      | no | Case-insensitive disease search. |
| `medicationId` | long        | no | Filter by medication id. |
| `drugType` | `DrugsType` | no | Filter by drug type. |
| `diagnosedByDoctorId` | UUID        | no | Filter by diagnosing doctor UUID. |
| `diagnosedPatientId` | Long           | no | Filter by diagnosed patient UUID as currently declared by the controller. |
| `diagnosedAfter` | date        | no | Include diagnoses on or after this date. |
| `diagnosedBefore` | date        | no | Include diagnoses on or before this date. |

`POST /api/diagnoses` and `POST /api/diagnoses/{id}` request body (`DiagnosisRequest` Object):

```json
{
  "disease": "Diabetes Mellitus Typ 2",
  "medication_id": 1,
  "diagnosed_by": "a5cb8acc-9836-40c5-8db3-63ba5b09d773",
  "diagnosed_patient": 1,
  "diagnosed_at": "2024-03-01"
}
```

## Common Responses

Successful `GET` collection endpoints usually return Spring `Page` JSON:

```json
{
  "content": [],
  "pageable": {},
  "totalElements": 0,
  "totalPages": 0,
  "last": true,
  "size": 20,
  "number": 0
}
```

Most `GET /{id}` endpoints return `404 NOT_FOUND` when the resource does not exist.

When combining `nameContains` and `name` filtering, a `400 BAD_REQUEST` error is being returned
