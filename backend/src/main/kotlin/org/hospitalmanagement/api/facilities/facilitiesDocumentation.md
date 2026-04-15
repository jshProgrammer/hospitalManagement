# Facilities
Base URL: http://localhost:8080
## Bookings
### GET
- **Endpoint**: `GET /api/facilities/bookings/1` 

Response:
```json
{
    "id": 1,
    "from": "2025-09-11",
    "until": "2025-10-23",
    "state": "COMPLETED",
    "room": {
        "id": 116,
        "station": {
            "id": 14,
            "name": "Psychiatrische Akutstation",
            "department": {
                "id": 14,
                "name": "Klinik und Poliklinik für Psychiatrie, Psychosomatik und Psychotherapie",
                "building": "3C"
            }
        },
        "number": 3146,
        "floor": 3,
        "beds": 2
    },
    "patient": {
        "id": 613,
        "person": {
            "id": "6efe7d24-3bc9-4de8-a981-7f859b01c59f",
            "gender": "d",
            "firstName": "Kiara",
            "lastName": "Murphy",
            "plz": 25480,
            "city": "Thompsonland",
            "street": "Richardson Well",
            "streetNo": 197,
            "country": "USA",
            "birthday": "1952-03-28",
            "phone": "015212948440",
            "email": "janet_pello@example.org"
        }
    }
}
```

Example calls with filtering and pagination:
- `GET /api/bookings`: get all bookings
- `GET /api/bookings?page=0&size=10`: get all bookings with pagination
- `GET /api/bookings?state=COMPLETED`: get all bookings with filtering

Please note that a `404 NOT FOUND` error will be returned if the booking with the specified ID does not exist.

### POST

#### Creating new booking
- **Endpoint**: `POST /api/facilities/bookings` 

In the header section:
```json
{
    "from": "2025-01-01",
    "until": "2025-02-02",
    "state": "COMPLETED",
    "room_id": 116,
    "patient_id": 613
}
```

#### Relocating patient
Please note that this functionality might be moved to /api/patients/PATIENTID/relocate and is not finished yet!
- **Endpoint**: `POST /api/bookings/PATIENTID/relocate`

In the header section:
```json
{
  "room_id": 1
}
```

#### Patient discharge
Please note that this functionality might be moved to /api/patients/PATIENTID/discharge and is not finished yet!
- **Endpoint**: `POST /api/bookings/PATIENTID/discharge`


## Departments
### GET
- **Endpoint**: `GET /api/departments/1`

Response:
```json
{
    "id": 1,
    "name": "Klinik und Poliklinik für Anästhesiologie, Intensivmedizin, Notfallmedizin und Schmerztherapie",
    "building": "4B"
}
```

Please note that different filtering options are provided:
- `nameContains`
- `name`
- `building`

Using a combination of `name` and `nameContains` is forbidden!

Example calls with filtering and pagination:
- `GET /api/departments`: get all bookings
- `GET /api/departments?page=0&size=10`: get all bookings with pagination
- `GET api/departments?nameContains=Kinder`: get all bookings with filtering
- `GET /api/departments?building=4C&nameContains=Kinder`: combine building and nameContains-filtering


## Rooms
### GET
- **Endpoint**: `GET /api/rooms/ROOMID`

Response:
```json
{
    "id": 1,
    "station": {
        "id": 1,
        "name": "Interdisziplinaere Intensivstation",
        "department": {
            "id": 1,
            "name": "Klinik und Poliklinik für Anästhesiologie, Intensivmedizin, Notfallmedizin und Schmerztherapie",
            "building": "4B"
        }
    },
    "number": 4011,
    "floor": 4,
    "beds": 1
}
```

Alternative requests:
- `GET /api/rooms`: get all rooms
- `GET /api/rooms/floor/FLOORNUMBER`: get all rooms of specific floor

## Stations

### GET
- **Endpoint**: `GET /api/stations/STATIONID` 

Response:
```json
{
  "id": 1,
  "name": "Interdisziplinaere Intensivstation",
  "department": {
    "id": 1,
    "name": "Klinik und Poliklinik für Anästhesiologie, Intensivmedizin, Notfallmedizin und Schmerztherapie",
    "building": "4B"
  }
}
```

Alternative requests with filtering and pagination:
- `GET /api/stations`: get all stations
- `GET /api/stations?name=Interdisziplinaere Intensivstation`: get station by exact name
- `GET /api/stations?nameContains=Neuro`: get station that contains string
- `GET /api/stations?departmentId=5`: get stations of specific department
- `GET /api/stations?departmentId=5&nameContains=HNO`: combining multiple filters
