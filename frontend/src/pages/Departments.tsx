import { useEffect, useState } from 'react'
import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'
import type { Department, DepartmentPage } from '../types/Department.tsx'
import { mapDepartment } from '../mapper/departmentMapper.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Department; header: string }[]

export function Departments() {
  const [departments, setDepartments] = useState<Department[]>([])

  useEffect(() => {
    async function fetchDepartments() {
      const response = await fetch('api/departments?sort=id,asc&size=30')
      const data: DepartmentPage = await response.json()

      setDepartments(data.content.map(mapDepartment))
    }

    fetchDepartments().catch(console.error)
  }, [])

  return (
    <MainPage title="Departments">
      <Table columns={columns} data={departments} />
    </MainPage>
  )
}