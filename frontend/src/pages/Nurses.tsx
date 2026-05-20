import MainPage from '../layout/MainPage.tsx'
import type { Nurse, NurseApi } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { nurseFilters } from '../constants/filters.tsx'
import { useState } from 'react'

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
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/nurses`)
  const { data, loading, error, reload, totalPages } = usePageData<NurseApi, Nurse>(
    url,
    page,
    mapNurse
  )

  return (
    <MainPage
      title="Nurses"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={nurseFilters} values={filters} onChange={setFilters} />}
    />
  )
}