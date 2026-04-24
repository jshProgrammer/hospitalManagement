# Medication
Base URL: http://localhost:8080

## Sections
- [Drugs](#drugs)
    - [GET](#get)
- [Doses](#doses)
    - [GET](#get-1)
    - [POST](#post)
    - [PUT](#put)

- [Medications](#medications)
    - [GET](#get-2)
    - [POST](#post-1)
    - [PUT](#put-1)
- [Diagnoses](#diagnoses)
    - [GET](#get-3)
    - [POST](#post-2)
    - [PUT](#put-2)

- - -

## Drugs
- **Endpoint**: `/api/drugs`

### GET

- `GET /api/drugs`: get all drugs
- `GET /api/drugs/{DRUG_ID}`: get drug with specific id
- `GET /api/drugs?page=0&size=10`: get all drugs with pagination
- `GET /api/drugs?activeIngredient=Insulin`: get all drugs with filtering

Please note that different filtering options are provided:
- `nameContains`
- `name`
- `activeIngredient`
- `type` (case-insensitive)

Using a combination of `name` and `nameContains` is forbidden!

In the future, `criticalAmountInDays`-filtering based on stock and current medications will be added as well!

Please note that a `404 NOT FOUND` error will be returned if the drug with the specified ID does not exist.

Example response for `GET /api/drugs/1`:
```json
{
    "id": 1,
    "stock": 2013,
    "name": "Paracetamol",
    "activeIngredient": "Paracetamol",
    "type": "SYRUP"
}
```

- - -

## Doses
- **Endpoint**: `/api/doses`

### GET

- `GET /api/doses`: get all doses
- `GET /api/doses/{DOSE_ID}`: get dose with specific id
- `GET /api/doses?page=0&size=10`: get all doses with pagination
- `GET /api/doses?unit=MG&frequency=X_DAILY`: get all doses with filtering

Please note that different filtering options are provided:
- `unit` (enum)
- `frequency` (enum)
- `amount`
- `frequencyAmount`

Please note that a `404 NOT FOUND` error will be returned if the dose with the specified ID does not exist.

Example response for `GET /api/doses/1`:
```json
{
  "id": 1,
  "unit": "G",
  "amount": 3,
  "frequency": "X_WEEKLY",
  "frequencyAmount": 2
}
```

- - -

## Medications
- **Endpoint**: `/api/medications`

### GET

- `GET /api/medications`: get all medications
- `GET /api/medications/{MEDICATION_ID}`: get medication with specific id
- `GET /api/medications?page=0&size=10`: get all medications with pagination
- `GET /api/medications?active=true`: get all currently active medications (no ended date)
- `GET /api/medications?drugType=SYRUP&startedAfter=2024-01-01`: get all medications with filtering

Please note that different filtering options are provided:
- `drugId`
- `drugType` (enum)
- `doseUnit` (enum)
- `startedAfter` (ISO date, e.g. `2024-01-01`)
- `startedBefore` (ISO date, e.g. `2024-12-31`)
- `active` (`true` = no ended date, `false` = has ended date)

Please note that a `404 NOT FOUND` error will be returned if the medication with the specified ID does not exist.

Example response for `GET /api/medications/1`:
```json
{
  "id": 1,
  "dose": {
    "id": 324,
    "unit": "G",
    "amount": 2,
    "frequency": "X_WEEKLY",
    "frequencyAmount": 7
  },
  "drug": {
    "id": 635,
    "stock": 1384,
    "name": "Quetiapine",
    "activeIngredient": "Quetiapine",
    "type": "CREAM"
  },
  "started": "2021-07-26T22:00:00.000+00:00",
  "ended": "2021-11-22T23:00:00.000+00:00"
}
```

### POST

Post funktioniert nicht, idk why
- **Endpoint**: `POST /api/medications`

Request body:
```json
{
  "dose_id": 1,
  "drug_id": 1,
  "started": "2024-03-01",
  "ended": null
}
```

### PUT

- **Endpoint**: `PUT /api/medications/{MEDICATION_ID}`

Please note that a `404 NOT FOUND` error will be returned if the medication with the specified ID does not exist.

Request body:
```json
{
  "dose_id": 1,
  "drug_id": 1,
  "started": "2024-03-01",
  "ended": "2024-06-01"
}
```

- - -

## Diagnoses
- **Endpoint**: `/api/diagnoses`

### GET

- `GET /api/diagnoses`: get all diagnoses
- `GET /api/diagnoses/{DIAGNOSIS_ID}`: get diagnosis with specific id
- `GET /api/diagnoses?page=0&size=10`: get all diagnoses with pagination
- `GET /api/diagnoses?diseaseContains=Diabetes&drugType=TABLET`: get all diagnoses with filtering

Please note that different filtering options are provided:
- `disease` (exact match, case-insensitive)
- `diseaseContains` (partial match, case-insensitive)
- `medicationId`
- `drugType` (enum)
- `diagnosedByDoctorId` (UUID)
- `diagnosedPatientId` (UUID — uses the Person ID, not the Patient ID)
- `diagnosedAfter` (ISO date, e.g. `2024-01-01`)
- `diagnosedBefore` (ISO date, e.g. `2024-12-31`)

Using a combination of `disease` and `diseaseContains` is forbidden!

Please note that a `404 NOT FOUND` error will be returned if the diagnosis with the specified ID does not exist.

Example response for `GET /api/diagnoses/1`:
```json
{
  "id": 1,
  "disease": "adrenal adenoma",
  "medication": {
    "id": 8253,
    "dose": {
      "id": 5160,
      "unit": "L",
      "amount": 2,
      "frequency": "EVERY_X_WEEKS",
      "frequencyAmount": 12
    },
    "drug": {
      "id": 429,
      "stock": 1628,
      "name": "Asenapine",
      "activeIngredient": "Asenapine",
      "type": "INFUSION"
    },
    "started": "2024-03-25T23:00:00.000+00:00",
    "ended": "2024-08-31T22:00:00.000+00:00"
  },
  "diagnosedBy": {
    "id": "0436b763-b640-4fbd-b7f4-1ce539ac40ca",
    "employee": {
      "id": "0436b763-b640-4fbd-b7f4-1ce539ac40ca",
      "person": {
        "id": "597022e6-3566-4bd0-8b74-fd58b76dea8e",
        "gender": "f",
        "firstName": "Kiara",
        "lastName": "Pellitus",
        "plz": 27163,
        "city": "Andrewburgh",
        "street": "Roberson Mountains",
        "streetNo": 2222,
        "country": "USA",
        "birthday": "1936-03-24",
        "phone": "015120614192",
        "email": "cynthia.pellucid@swainlabs.test"
      },
      "department": 10
    },
    "work_phone": "49931201100330",
    "type": "RESIDENT"
  },
  "diagnosedPatient": {
    "id": "36bc3701-225b-457a-b268-257c36dce065",
    "gender": "m",
    "firstName": "Lacey",
    "lastName": "Pellum",
    "plz": 32942,
    "city": "Rioschester",
    "street": "Osborne Mews",
    "streetNo": 1972,
    "country": "USA",
    "birthday": "1945-07-16",
    "phone": "015784699114",
    "email": "gilbert.pearson@swain.invalid"
  },
  "diagnosedAt": "2024-03-24T23:00:00.000+00:00"
}
```

### POST

Geht noch nicht

- **Endpoint**: `POST /api/diagnoses`

Request body (UUIDs mit echten ersetzen):
```json
{
  "disease": "Diabetes Mellitus Typ 2",
  "medication_id": 1,
  "diagnosed_by": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "diagnosed_patient": "6efe7d24-3bc9-4de8-a981-7f859b01c59f",
  "diagnosed_at": "2024-03-01"
}
```

### PUT

- **Endpoint**: `PUT /api/diagnoses/{DIAGNOSIS_ID}`

Please note that a `404 NOT FOUND` error will be returned if the diagnosis with the specified ID does not exist.

Request body (UUIDs mit echten ersetzen):
```json
{
  "disease": "Diabetes Mellitus Typ 1",
  "medication_id": 1,
  "diagnosed_by": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "diagnosed_patient": "6efe7d24-3bc9-4de8-a981-7f859b01c59f",
  "diagnosed_at": "2024-03-01"
}
```

