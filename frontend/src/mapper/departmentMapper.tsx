import type { Department, DepartmentApi } from '../types/Department.tsx'

export function mapDepartment(department: DepartmentApi): Department {
  return {
    id: department.id,
    name: department.name,
    building: department.building,
  }
}