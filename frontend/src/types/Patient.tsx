export type PersonApi = {
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

export type PatientApi = {
  id: number
  person: PersonApi
}

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

export type PatientCreateRequest = {
  gender: string
  firstName: string
  lastName: string
  email: string
  phoneNumber: string
  plz: string
  city: string
  street: string
  houseNumber: string
  country: string
  birthday: string
}

export type PersonSearchResult = {
  id: number
  firstName: string
  lastName: string
  birthday: string
  isEmployee: boolean
  isPatient: boolean
}

export type PatientRequest = {
  id: number
  personId: number
}

export type PatientCreationResponse = {
  potentialMatches?: PersonSearchResult[] | null
  createdPatient?: PatientRequest | null
}
