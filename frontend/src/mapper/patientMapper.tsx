import type { Patient, PatientApi } from '../types/Patient.tsx'

export const mapPatient = (patient: PatientApi): Patient => ({
  id: patient.id,
  personId: patient.person.id,
  gender: patient.person.gender,
  firstName: patient.person.firstName,
  lastName: patient.person.lastName,
  plz: patient.person.plz,
  city: patient.person.city,
  street: patient.person.street,
  streetNo: patient.person.streetNo,
  country: patient.person.country,
  birthday: patient.person.birthday,
  phone: patient.person.phone,
  email: patient.person.email,
})