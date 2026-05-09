import MainPage from '../layout/MainPage.tsx'
import type { Department, DepartmentApi } from '../types/Department.tsx'
import { mapDepartment } from '../mapper/departmentMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Department; header: string }[]

export function Departments() {
  const { data, loading, error } = usePageData<DepartmentApi, Department>(
    '/api/departments?sort=id,asc&size=30',
    mapDepartment
  )

  return (
    <MainPage title="Departments" columns={columns} data={data} loading={loading} error={error} />
  )
}