import MainPage from '../layout/MainPage.tsx'
import type { Doctor, DoctorApi } from '../types/Doctor.tsx'
import { mapDoctor } from '../mapper/doctorMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { doctorFilters } from '../constants/filters.tsx'
import { useState } from 'react'
import { personColumns } from '../constants/colums.tsx'

const columns = [...personColumns, 'department', 'workPhone', 'type'] satisfies (keyof Doctor)[]

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