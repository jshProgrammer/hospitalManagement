import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'

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
  const { data, loading, error, reload } = usePageData<PatientApi, Patient>(
    '/api/patients?sort=id,asc&size=30',
    mapPatient
  )
  return (
    <MainPage
      title="Patients"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
    />
  )
}