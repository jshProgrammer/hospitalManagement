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