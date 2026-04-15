# Facilities
Base URL: http://localhost:8080
## Booking
### GET
- **Endpoint**: `GET /api/facilities/bookings/1` 
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
- `GET /api/facilities/bookings`: get all bookings
- `GET /api/bookings?page=0&size=10`: get all bookings with pagination
- `GET /api/bookings?state=OPEN`: get all bookings with filtering

Please note that a `404 NOT FOUND` error will be returned if the booking with the specified ID does not exist.

