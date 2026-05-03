import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'

type Patient = {
  id: number
  person_id: string
  gender: string
  firstname: string
  lastname: string
  plz: number
  city: string
  street: string
  street_no: number
  country: string
  birthdate: string
  phone: string
  email: string
}

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'person_id', header: 'Person ID' },
  { key: 'gender', header: 'Geschlecht' },
  { key: 'firstname', header: 'Vorname' },
  { key: 'lastname', header: 'Nachname' },
  { key: 'plz', header: 'PLZ' },
  { key: 'city', header: 'Stadt' },
  { key: 'street', header: 'Straße' },
  { key: 'street_no', header: 'Hausnummer' },
  { key: 'country', header: 'Land' },
  { key: 'birthdate', header: 'Geburtsdatum' },
  { key: 'phone', header: 'Telefon' },
  { key: 'email', header: 'E-Mail' },
] satisfies { key: keyof Patient; header: string }[]

const patients: Patient[] = [
  {
    id: 1,
    person_id: '550e8400-e29b-41d4-a716-446655440000',
    gender: 'm',
    firstname: 'Max',
    lastname: 'Mustermann',
    plz: 80331,
    city: 'München',
    street: 'Marienplatz',
    street_no: 1,
    country: 'Deutschland',
    birthdate: '1990-05-12',
    phone: '0891234567',
    email: 'max.mustermann@example.com',
  },
  {
    id: 2,
    person_id: 'c1a5f6e2-7d3a-4f91-9c0b-2a6d5f9e8b11',
    gender: 'f',
    firstname: 'Anna',
    lastname: 'Schmidt',
    plz: 10115,
    city: 'Berlin',
    street: 'Invalidenstraße',
    street_no: 42,
    country: 'Deutschland',
    birthdate: '1985-11-03',
    phone: '0309876543',
    email: 'anna.schmidt@example.com',
  },
]

export function Patients() {
  return <MainPage title={'Patients'} children={<Table columns={columns} data={patients} />} />
}