import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFilters } from '../constants'

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
  const { filters, setFilters, url } = useTableFilters('/api/patients?sort=id,asc&size=30')
  const { data, loading, error, reload } = usePageData<PatientApi, Patient>(url, mapPatient)

  return (
    <MainPage
      title="Patients"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      filters={<TableFilters fields={patientFilters} values={filters} onChange={setFilters} />}
    />
  )
}
