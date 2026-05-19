import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFilters } from '../constants/filters.tsx'
import { DEFAULT_PAGE_SIZE } from '../constants/pagination.tsx'

const columns = [
  { key: 'firstName', header: 'Vorname' },
  { key: 'lastName', header: 'Nachname' },
  { key: 'gender', header: 'Geschlecht' },
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
  const { filters, setFilters, url } = useTableFilters(
    `/api/patients?sort=id,asc&size=${DEFAULT_PAGE_SIZE}`
  )
  const { data, loading, error, reload } = usePageData<PatientApi, Patient>(url, mapPatient)

  return (
    <MainPage
      title="Patients"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      rowStart={0 * DEFAULT_PAGE_SIZE}
      filters={<TableFilters fields={patientFilters} values={filters} onChange={setFilters} />}
    />
  )
}