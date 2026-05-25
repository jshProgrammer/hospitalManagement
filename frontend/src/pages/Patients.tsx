import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFilters } from '../constants/filters.ts'
import { useState } from 'react'
import { personColumns } from '../constants/columns.ts'

const columns = [...personColumns] satisfies (keyof Patient)[]

export function Patients() {
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/patients`)
  const { data, loading, error, reload, totalPages } = usePageData<PatientApi, Patient>(
    url,
    page,
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
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={patientFilters} values={filters} onChange={setFilters} />}
    />
  )
}