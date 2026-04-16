# Medication
Base URL: http://localhost:8080

## Drugs
- **Endpoint**: `/api/drugs`

### GET

- `GET /api/drugs`: get all drugs
- `GET /api/drugs/{DRUG_ID}`: get drugs with specific id
- `GET /api/drugs?page=0&size=10`: get all drugs with pagination
- `GET /api/drugs?activeIngredient=Insulin`: get all drugs with filtering

Please note that different filtering options are provided:
- `nameContains`
- `name`
- `activeIngredient`

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
