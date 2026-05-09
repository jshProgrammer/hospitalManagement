export type DepartmentApi = {
  id: number
  name: string
  building: string
}

export type DepartmentPage = {
  content: DepartmentApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Department = {
  id: number
  name: string
  building: string
}