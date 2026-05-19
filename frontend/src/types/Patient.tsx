export type Patient = {
  id: number
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
}

export type PatientPage = {
  content: PatientApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}

export type PatientApi = {
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
}