# Hospital Management Backend API

Base URL when running locally: `http://localhost:8080`

Collection endpoints use cursor-based (keyset) pagination. See [Pagination](#pagination) for the common parameters and response shape.

Date values are accepted as ISO date strings unless noted otherwise (datetime used in Medication, Diagnosis and Booking), for example `2024-03-01`.

## Sections
- [Pagination](#pagination)
- [Enviroment](#environment)
- [Security](#security)
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


## Pagination

All collection list endpoints use cursor-based (keyset) pagination instead of offset/page-number pagination. This avoids the COUNT query and remains efficient on large, append-only tables.

**Common query parameters** (all paginated endpoints):

| Name | Type | Required | Default | Description |
| --- | --- | --- | --- | --- |
| `after` | long | no | — | Return only records whose `id` is greater than this value. Omit (or pass `0`) to start from the beginning. |
| `limit` | int | no | `10` | Maximum number of records to return per page. |

**Common response envelope** (all paginated endpoints):

```json
{
  "data": [],
  "nextCursor": 42,
  "hasMore": true
}
```

The `data` key name matches the resource type (e.g. `patients`, `doctors`, `medications`).  
`nextCursor` is the `id` of the last returned record (or `0` when the result is empty). Pass it as `after` in the next request to fetch the following page.  
`hasMore` is `true` when more records exist beyond the current page.

## Environment

The application reads database connection values from `.env` or environment variables:

```properties
DATABASE_URL=jdbc:postgresql://host:5432/database
DATABASE_USER=username
DATABASE_PASSWORD=password
```

## Security

### Rate Limiting

The patient search endpoint is protected against dictionary attacks on blind indices (see [Encrypted Fields & Blind Indexing](#encrypted-fields--blind-indexing)). Because blind indices are deterministic, an attacker with read access to the database could attempt to brute-force names or postal codes by issuing repeated search queries and comparing hashes. Rate limiting prevents this.

| Endpoint | Limit | Scope |
| --- | --- | --- |
| `GET /api/patients` | 10 requests / minute | per IP address |

Requests exceeding the limit receive `429 Too Many Requests`. The bucket refills fully after 60 seconds.

### Input Validation

`plz` and `birthday` are validated on all person-creation endpoints (`POST /api/patients/new`, `POST /api/doctors/new`, `POST /api/nurses/new`) before the values are hashed and encrypted. This ensures the blind index always receives input in a consistent format — differing formats (e.g. `"12.03.1990"` vs `"1990-03-12"`) would produce different hashes for the same person and break exact-match search.

| Field | Rule | Example |
| --- | --- | --- |
| `plz` | Exactly 5 digits (`^[0-9]{5}$`) | `"97070"` |
| `birthday` | ISO date `YYYY-MM-DD`, must be in the past | `"1990-03-12"` |

Invalid values return `400 Bad Request` with a structured error body — see [Common Responses](#common-responses).

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
| `after` | long | no | Cursor — return records with `id > after`. |
| `limit` | int | no | Page size (default `10`). |
| `state` | `BookingState` | no | Filter by booking state. |

Response: `{ "bookings": [...], "nextCursor": <long>, "hasMore": <bool> }`

Bookings are returned newest first by `from` date, then by id.

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

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/patients/new` | Create a patient from new person data, or return potential duplicate person matches. |
| `POST` | `/api/patients/new/{personId}` | Create a patient from an existing person id. |
| `GET` | `/api/patients` | List/search patients (cursor-paginated). |
| `GET` | `/api/patients/{id}` | Get one patient by patient id. |
| `GET` | `/api/patients/{id}/bookings` | List bookings for a patient (cursor-paginated). |
| `POST` | `/api/patients/{id}/discharge` | Discharge a patient from the current booking. |
| `POST` | `/api/patients/{id}/relocate` | Relocate a patient to another room. |
| `GET` | `/api/patients/{id}/diagnoses` | List diagnoses for a patient (cursor-paginated). |

`GET /api/patients` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `after` | long | no | Cursor — return records with `id > after`. |
| `limit` | int | no | Page size (default `10`). |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | string | no | Exact birthday (ISO date string, e.g. `2005-07-16`). Matched via blind index — only exact matches, no range queries. |
| `plz` | string | no | Postal code (e.g. `"97070"`). Matched via blind index — only exact matches. |
| `street` | string | no | Exact street. Matched via blind index — only exact matches. |
| `streetNo` | int | no | House number. |
| `bookingStatus` | `PatientBookingStatusFilter` | no | Repeatable filter for booking state groups. Supported values: `CHECKED_IN` for bookings with state `CHECKED_IN` whose date range includes today, `UPCOMING` for future bookings with state `PENDING` or `CONFIRMED`. Example: `?bookingStatus=CHECKED_IN&bookingStatus=UPCOMING`. |

> **Note on encrypted fields:** `firstName`, `lastName`, `plz`, `city`, `street`, `birthday`, `phone`, and `email` are stored encrypted. Each field is accompanied by a corresponding `*Hash` field (e.g. `firstNameHash`, `plzHash`) used for blind-index filtering. 
> All these fields are returned in plaintext in the response alongside their hash. 
> Filtering is supported via exact match only — `LIKE` / partial and range queries (`<=`, `>=`) are **not** supported for these fields.

Response: `{ "patients": [...], "nextCursor": <long>, "hasMore": <bool> }`

Patient booking status filters are evaluated through the booking-patient relationship and do not use encrypted person fields.

`GET /api/patients/{id}/bookings` and `GET /api/patients/{id}/diagnoses` accept `after` (long) and `limit` (int, default `10`) and return the same cursor envelope with keys `bookings`/`diagnoses`. Patient bookings are returned newest first by `from` date, then by id.

`POST /api/patients/new` request body (`PersonCreateRequest` Object):

```json
{
  "gender": "f",
  "firstName": "Lacey",
  "lastName": "Pellum",
  "email": "lacey.pellum@example.invalid",
  "phoneNumber": "015784699114",
  "plz": "97070",
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
| `POST` | `/api/doctors/new/{personId}` | Create a doctor from an existing person id and employee data. |
| `GET` | `/api/doctors` | List/search doctors. |
| `GET` | `/api/doctors/{id}` | Get one doctor by id. |

`GET /api/doctors` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `after` | long | no | Cursor — return records with `id > after`. |
| `limit` | int | no | Page size (default `10`). |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | string | no | Exact birthday (ISO date string, e.g. `2005-07-16`). Matched via blind index — only exact matches. |
| `plz` | string | no | Postal code (e.g. `"97070"`). Matched via blind index — only exact matches. |
| `street` | string | no | Exact street. Matched via blind index — only exact matches. |
| `streetNo` | int | no | House number. |
| `type` | `DoctorsType` | no | Filter by doctor type. |
| `departmentId` | long | no | Filter by department id. |
| `workPhone` | string | no | Exact work phone. |

Response: `{ "doctors": [...], "nextCursor": <long>, "hasMore": <bool> }`

`POST /api/doctors/new` request body (`DoctorCreationRequest` Object):

```json
{
  "gender": "m",
  "firstName": "Alex",
  "lastName": "Morgan",
  "email": "alex.morgan@example.invalid",
  "phoneNumber": "015123456789",
  "plz": "97070",
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
| `POST` | `/api/nurses/new/{personId}` | Create a nurse from an existing person id and employee data. |
| `GET` | `/api/nurses` | List/search nurses. |
| `GET` | `/api/nurses/{id}` | Get one nurse by id. |
| `GET` | `/api/nurses/station/{stationId}` | List nurses for one station. |

`GET /api/nurses` query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| `after` | long | no | Cursor — return records with `id > after`. |
| `limit` | int | no | Page size (default `10`). |
| `firstName` | string | no | Exact first name. |
| `lastName` | string | no | Exact last name. |
| `email` | string | no | Exact email. |
| `phone` | string | no | Exact phone. |
| `gender` | `Gender` | no | Filter by gender. |
| `city` | string | no | Exact city. |
| `country` | string | no | Exact country. |
| `birthday` | string | no | Exact birthday (ISO date string, e.g. `2005-07-16`). Matched via blind index — only exact matches. |
| `plz` | string | no | Postal code (e.g. `"97070"`). Matched via blind index — only exact matches. |
| `street` | string | no | Exact street. Matched via blind index — only exact matches. |
| `streetNo` | int | no | House number. |
| `stationId` | long | no | Filter by station id. |
| `departmentId` | long | no | Filter by department id. |

Response: `{ "nurses": [...], "nextCursor": <long>, "hasMore": <bool> }`

`POST /api/nurses/new` request body (`NurseCreationRequest` Object):

```json
{
  "gender": "f",
  "firstName": "Jamie",
  "lastName": "Reed",
  "email": "jamie.reed@example.invalid",
  "phoneNumber": "015987654321",
  "plz": "97070",
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
| `after` | long | no | Cursor — return records with `id > after`. |
| `limit` | int | no | Page size (default `10`). |
| `drugId` | long | no | Filter by drug id. |
| `drugType` | `DrugsType` | no | Filter by drug type. |
| `doseUnit` | `DoseUnit` | no | Filter by dose unit. |
| `startedAfter` | date | no | Include medications started on or after this date. |
| `startedBefore` | date | no | Include medications started on or before this date. |
| `active` | boolean | no | `true` for no end date, `false` for ended medications. |

Response: `{ "medications": [...], "nextCursor": <long>, "hasMore": <bool> }`

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
| `after` | long        | no | Cursor — return records with `id > after`. |
| `limit` | int         | no | Page size (default `10`). |
| `disease` | string      | no | Exact disease. |
| `diseaseContains` | string      | no | Case-insensitive disease search. |
| `medicationId` | long        | no | Filter by medication id. |
| `drugType` | `DrugsType` | no | Filter by drug type. |
| `diagnosedByDoctorId` | Long        | no | Filter by diagnosing doctor id. |
| `diagnosedPatientId` | Long           | no | Filter by diagnosed patient id. |
| `diagnosedAfter` | date        | no | Include diagnoses on or after this date. |
| `diagnosedBefore` | date        | no | Include diagnoses on or before this date. |

Response: `{ "diagnoses": [...], "nextCursor": <long>, "hasMore": <bool> }`

`POST /api/diagnoses` and `POST /api/diagnoses/{id}` request body (`DiagnosisRequest` Object):

```json
{
  "disease": "Diabetes Mellitus Typ 2",
  "medication_id": 1,
  "diagnosed_by": 1,
  "diagnosed_patient": 1,
  "diagnosed_at": "2024-03-01"
}
```

## Common Responses

Successful `GET` collection endpoints return a cursor envelope. The key name matches the resource type:

```json
{
  "patients": [],
  "nextCursor": 0,
  "hasMore": false
}
```

Pass `nextCursor` as `after` in the next request to fetch the following page. When `hasMore` is `false`, you have reached the end of the result set.

Most `GET /{id}` endpoints return `404 NOT_FOUND` when the resource does not exist.

When combining `nameContains` and `name` filtering, a `400 BAD_REQUEST` error is returned.

Input validation failures on person-creation endpoints return `400 Bad Request` with this body:

```json
{
  "status": 400,
  "error": "Validierungsfehler",
  "fields": {
    "plz": "PLZ muss genau 5 Ziffern enthalten (z.B. 80331)",
    "birthday": "Geburtsdatum muss im Format YYYY-MM-DD vorliegen und in der Vergangenheit liegen"
  }
}
```

Rate limit exceeded on `GET /api/patients` returns `429 Too Many Requests`:

```json
{
  "error": "Zu viele Suchanfragen. Maximal 10 Anfragen pro Minute pro IP erlaubt."
}
```

### Encrypted Fields & Blind Indexing

Several fields on the `person` object are stored encrypted at rest. Each encrypted field is returned in plaintext alongside a corresponding `*Hash` field containing the blind index used for exact-match filtering. The following fields are encrypted:

`firstName`, `lastName`, `plz`, `city`, `street`, `birthday`, `phone`, `email`

**Filtering restrictions:** These fields support **exact match only**. `LIKE`/contains queries and range comparisons (`<=`, `>=`) are **not** supported.

Example `patients` response with encrypted fields:

```json
{
  "patients": [
    {
      "id": 7,
      "person": {
        "id": 8,
        "gender": "f",
        "firstName": "Rollie",
        "lastName": "Odin",
        "firstNameHash": "pnyvBXxaJsja6ASh2wI0e8TeryTVC/jQtuOSEJ5rFnI=",
        "lastNameHash": "5+gXECqroUfYZgi94PxjhMzWyl+BM9+Yaw9IWiXJE9Y=",
        "plz": "97070",
        "plzHash": "aE28sYo8FHX/Gsprb/inLzYXvpMRsr2ctqB2prUwqV8=",
        "city": "Wuerzburg",
        "cityHash": "keDbD7B6sMZoIWm0dhka7q9zJg39Kj+6dB1EbJxS2ak=",
        "street": "Tolle strasse",
        "streetHash": "OF1GLqsZh+vu9QvVHRjY5EHvPwJJ4sZr7t9DQyuYEA0=",
        "streetNo": 12,
        "country": "Germany",
        "birthday": "2005-07-16",
        "birthdayHash": "ljHlYU2sdGnhVNMttLvJkpteds+rO81fNaDNQDRLZUk=",
        "phone": "015784699114",
        "phoneHash": "Ov9CKBCBfjdPXdkkFvk/tgQ9pLV6JcvKz1WHgX25aZI=",
        "email": "knirbs@gmail.com",
        "emailHash": "+kJtIuKm9TEaxIvwdxJHNcbDW6NatFlR4WUk2UIlj4k="
      }
    }
  ],
  "nextCursor": 7,
  "hasMore": false
}
```
