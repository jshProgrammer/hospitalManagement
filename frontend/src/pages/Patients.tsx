import { useEffect, useState } from 'react'
import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'
import type { Patient, PatientPage } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'personId', header: 'Person ID' },
  { key: 'gender', header: 'Geschlecht' },
  { key: 'firstName', header: 'Vorname' },
  { key: 'lastName', header: 'Nachname' },
  { key: 'plz', header: 'PLZ' },
  { key: 'city', header: 'Stadt' },
  { key: 'street', header: 'Straße' },
  { key: 'streetNo', header: 'Hausnummer' },
  { key: 'country', header: 'Land' },
  { key: 'birthday', header: 'Geburtsdatum' },
  { key: 'phone', header: 'Telefon' },
  { key: 'email', header: 'E-Mail' },
] satisfies { key: keyof Patient; header: string }[]

export function Patients() {
  const [patients, setPatients] = useState<Patient[]>([])

  useEffect(() => {
    async function fetchPatients() {
      const response = await fetch('api/patients')
      const data: PatientPage = await response.json()

      setPatients(data.content.map(mapPatient))
    }
    fetchPatients().catch(console.error)
  }, [])

  return (
    <MainPage title="Patients">
      <Table columns={columns} data={patients} />
    </MainPage>
  )
}
