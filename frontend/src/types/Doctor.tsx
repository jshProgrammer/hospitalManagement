export type DoctorApi = {
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
  workPhone: string
  type: string
}