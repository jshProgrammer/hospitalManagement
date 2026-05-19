export type NurseApi = {
  id: number
  employee: {
    id: number
    person: {
      id: number
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
    }
    department: number
  }
  station?: {
    id: number
    name: string
    department: {
      id: number
      name: string
      building: string
    }
  } | null
}

export type NursePage = {
  content: NurseApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Nurse = {
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

  stationId?: number
  stationName?: string
  stationDepartmentId?: number
  stationDepartmentName?: string
  building?: string
}