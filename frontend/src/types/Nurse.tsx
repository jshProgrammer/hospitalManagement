export type NurseApi = {
  id: string
  employee: {
    id: string
    person: {
      id: string
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
  station: {
    id: number
    name: string
    department: {
      id: number
      name: string
      building: string
    }
  }
}

export type NursePage = {
  content: NurseApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type Nurse = {
  id: string
  employeeId: string
  personId: string
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
  stationId: number
  stationName: string
  stationDepartmentId: number
  stationDepartmentName: string
  building: string
}