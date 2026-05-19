import MainPage from '../layout/MainPage.tsx'
import type { Department, DepartmentApi } from '../types/Department.tsx'
import { mapDepartment } from '../mapper/departmentMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { departmentFilters } from '../constants/filters.tsx'
import { DEFAULT_PAGE_SIZE } from '../constants/pagination.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Department; header: string }[]

export function Departments() {
  const { filters, setFilters, url } = useTableFilters(
    `/api/departments?sort=id,asc&size=${DEFAULT_PAGE_SIZE}`
  )
  const { data, loading, error, reload } = usePageData<DepartmentApi, Department>(
    url,
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
      rowStart={0 * DEFAULT_PAGE_SIZE}
      filters={<TableFilters fields={departmentFilters} values={filters} onChange={setFilters} />}
    />
  )
}