import MainPage from '../layout/MainPage.tsx'
import type { Department, DepartmentApi } from '../types/Department.tsx'
import { mapDepartment } from '../mapper/departmentMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { departmentFilters } from '../constants/filters.tsx'
import { useState } from 'react'

const columns = [
  { key: 'name', header: 'Name' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Department; header: string }[]

export function Departments() {
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/departments`)
  const { data, loading, error, reload, totalPages } = usePageData<DepartmentApi, Department>(
    url,
    page,
    mapDepartment
  )

  return (
    <MainPage
      title="Departments"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={departmentFilters} values={filters} onChange={setFilters} />}
    />
  )
}