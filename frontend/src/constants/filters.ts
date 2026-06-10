import type { FilterField } from '../components/TableFilters'

export const genderOptions = [
  { label: 'Male', value: 'm' },
  { label: 'Female', value: 'f' },
  { label: 'Diverse', value: 'd' },
]

export const doctorTypeOptions = [
  { label: 'Assistant Physician', value: 'ASSISTANT_PHYSICIAN' },
  { label: 'Senior Physician', value: 'SENIOR_PHYSICIAN' },
  { label: 'Chief Physician', value: 'CHIEF_PHYSICIAN' },
  { label: 'Consultant', value: 'CONSULTANT' },
  { label: 'Resident', value: 'RESIDENT' },
  { label: 'Attending Physician', value: 'ATTENDING_PHYSICIAN' },
  { label: 'Head of Department', value: 'HEAD_OF_DEPARTMENT' },
]

export const drugTypeOptions = [
  { label: 'Tablet', value: 'tablet' },
  { label: 'Capsule', value: 'capsule' },
  { label: 'Syrup', value: 'syrup' },
  { label: 'Injection', value: 'injection' },
  { label: 'Infusion', value: 'infusion' },
  { label: 'Ointment', value: 'ointment' },
  { label: 'Cream', value: 'cream' },
  { label: 'Drops', value: 'drops' },
  { label: 'Spray', value: 'spray' },
  { label: 'Suppository', value: 'suppository' },
]

export const patientPersonFilters: FilterField[] = [
  { name: 'firstName', label: 'First name', placeholder: 'Marie' },
  { name: 'lastName', label: 'Last name', placeholder: 'Meyer' },
  { name: 'email', label: 'E-Mail', placeholder: 'name@example.com' },
  { name: 'phone', label: 'Phone', placeholder: '+49...' },
  { name: 'city', label: 'City', placeholder: 'Wuerzburg' },
  { name: 'country', label: 'Country', placeholder: 'Germany' },
  { name: 'birthday', label: 'Birthday', type: 'date' },
  { name: 'plz', label: 'PLZ', type: 'number', placeholder: '97070' },
  { name: 'street', label: 'Street', placeholder: 'Musterstrasse' },
  { name: 'streetNo', label: 'House no.', type: 'number', placeholder: '12' },
]

export const employeePersonFilters: FilterField[] = [
  ...patientPersonFilters.slice(0, 4),
  { name: 'gender', label: 'Gender', type: 'select', options: genderOptions },
  ...patientPersonFilters.slice(4),
]

export const patientFilters = patientPersonFilters

export const patientFiltersWithStatus: FilterField[] = [
  ...patientPersonFilters,
  {
    name: 'currentlyStationary',
    label: 'Room status',
    type: 'select',
    options: [{ label: 'Currently in room', value: 'true' }],
  },
]

export const doctorFilters: FilterField[] = [
  ...employeePersonFilters,
  { name: 'type', label: 'Doctor type', type: 'select', options: doctorTypeOptions },
  { name: 'departmentId', label: 'Department ID', type: 'number', placeholder: '1' },
  { name: 'workPhone', label: 'Work phone', placeholder: '+49...' },
]

export const nurseFilters: FilterField[] = [
  ...employeePersonFilters,
  { name: 'stationId', label: 'Station ID', type: 'number', placeholder: '1' },
  { name: 'departmentId', label: 'Department ID', type: 'number', placeholder: '1' },
]

export const departmentFilters: FilterField[] = [
  { name: 'nameContains', label: 'Name contains', placeholder: 'Cardio' },
  { name: 'name', label: 'Exact name', placeholder: 'Cardiology' },
  { name: 'building', label: 'Building', placeholder: 'A' },
]

export const stationFilters: FilterField[] = [
  { name: 'nameContains', label: 'Name contains', placeholder: 'North' },
  { name: 'name', label: 'Exact name', placeholder: 'North Wing' },
  { name: 'departmentId', label: 'Department ID', type: 'number', placeholder: '1' },
]

export const drugFilters: FilterField[] = [
  { name: 'nameContains', label: 'Name contains', placeholder: 'Ibu' },
  { name: 'name', label: 'Exact name', placeholder: 'Ibuprofen' },
  { name: 'activeIngredient', label: 'Active ingredient', placeholder: 'Ibuprofen' },
  { name: 'type', label: 'Type', type: 'select', options: drugTypeOptions },
  { name: 'criticalAmountInDays', label: 'Critical days', type: 'number', placeholder: '14' },
]
