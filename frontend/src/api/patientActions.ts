import { requestJson } from './http'
import type {
  PatientCreationResponse,
  PatientCreateRequest,
  PatientRequest,
} from '../types/Patient'
import type { BookingApi, BookingCreateRequest, RelocateRequest } from '../types/Bookings'

export function createPatient(payload: PatientCreateRequest) {
  return requestJson<PatientCreationResponse>('/api/patients/new', {
    method: 'POST',
    body: payload,
  })
}

export function createPatientFromPerson(personId: number) {
  return requestJson<PatientRequest>(`/api/patients/new/${personId}`, {
    method: 'POST',
  })
}

export function createBooking(payload: BookingCreateRequest) {
  return requestJson<BookingApi>('/api/bookings', {
    method: 'POST',
    body: payload,
  })
}

export function dischargePatient(patientId: number) {
  return requestJson<BookingApi>(`/api/patients/${patientId}/discharge`, {
    method: 'POST',
  })
}

export function relocatePatient(patientId: number, payload: RelocateRequest) {
  return requestJson<BookingApi>(`/api/patients/${patientId}/relocate`, {
    method: 'POST',
    body: payload,
  })
}

export function relocateBooking(bookingId: number, payload: RelocateRequest) {
  return requestJson<BookingApi>(`/api/bookings/${bookingId}/relocate`, {
    method: 'POST',
    body: payload,
  })
}

export function completeBooking(bookingId: number) {
  return requestJson<BookingApi>(`/api/bookings/${bookingId}/complete`, {
    method: 'POST',
  })
}
