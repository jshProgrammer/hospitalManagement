import MainPage from '../layout/MainPage.tsx'
import type { Nurse, NurseApi } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { nurseFilters } from '../constants/filters.tsx'
import { useState } from 'react'
import { personColumns } from '../constants/columns.tsx'

const columns = [
  ...personColumns,
  'department',
  'stationId',
  'stationName',
  'stationDepartmentId',
  'stationDepartmentName',
  'building',
] satisfies (keyof Nurse)[]

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