import MainPage from '../layout/MainPage.tsx'
import type { Doctor, DoctorApi } from '../types/Doctor.tsx'
import { mapDoctor } from '../mapper/doctorMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { doctorFilters } from '../constants/filters.tsx'
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
  { key: 'workPhone', header: 'Diensttelefon' },
  { key: 'type', header: 'Typ' },
] satisfies { key: keyof Doctor; header: string }[]

export function Doctors() {
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/doctors`)
  const { data, loading, error, reload, totalPages } = usePageData<DoctorApi, Doctor>(
    url,
    page,
    mapDoctor
  )

  return (
    <MainPage
      title="Doctors"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={doctorFilters} values={filters} onChange={setFilters} />}
    />
  )
}