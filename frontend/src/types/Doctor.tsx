import type { PersonApi } from './Patient.tsx'

export type DoctorApi = {
  id: number
  employee: {
    id: number
    person: PersonApi
    department: number
  }
  workPhone: string
  type: string
}

export type DoctorPage = {
  content: DoctorApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Doctor = {
  id: number
  employeeId: number
  personId: number
  gender: string
  firstName: string
  lastName: string
  plz: number
  city: string
  street: string
  streetNo: number
  country: string
  birthday: string
  phone: string
  email: string
  department: number
  workPhone: string
  type: string
}