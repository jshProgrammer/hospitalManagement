import type { PatientApi } from './Patient'
import type { StationApi } from './Station.tsx'

export type RoomApi = {
  id: number
  station: StationApi
  number: number
  floor: number
  beds: number
}

export type BookingApi = {
  id: number
  from: string
  until: string
  state: string
  room: RoomApi
  patient: PatientApi
}

export type BookingsResponse = {
  bookings: BookingApi[]
}

export type BookingState =
  | 'PENDING'
  | 'CONFIRMED'
  | 'CHECKED_IN'
  | 'COMPLETED'
  | 'CANCELLED'
  | 'RELOCATED'
  | 'NO_SHOW'
  | 'CHECKED_OUT_EARLY'

export type BookingCreateRequest = {
  from: string
  until: string | null
  state: BookingState
  room_id: number
  patient_id: number
}

export type RelocateRequest = {
  room_id: number
}
