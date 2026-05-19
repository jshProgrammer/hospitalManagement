import MainPage from '../layout/MainPage.tsx'
import type { Nurse, NurseApi } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { nurseFilters } from '../constants/filters.tsx'
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
  { key: 'department', header: 'Abteilung' },
  { key: 'stationId', header: 'Stations-ID' },
  { key: 'stationName', header: 'Station' },
  { key: 'stationDepartmentId', header: 'Stations-Abteilungs-ID' },
  { key: 'stationDepartmentName', header: 'Stations-Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Nurse; header: string }[]

export function Nurses() {
  const { filters, setFilters, url } = useTableFilters(
    `/api/nurses?sort=id,asc&size=${DEFAULT_PAGE_SIZE}`
  )
  const { data, loading, error, reload } = usePageData<NurseApi, Nurse>(url, mapNurse)

  return (
    <MainPage
      title="Nurses"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      rowStart={0 * DEFAULT_PAGE_SIZE}
      filters={<TableFilters fields={nurseFilters} values={filters} onChange={setFilters} />}
    />
  )
}