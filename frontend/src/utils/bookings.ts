import type { BookingApi } from '../types/Bookings'

const currentRoomBookingStates = new Set(['CONFIRMED', 'CHECKED_IN'])

export function hasCurrentRoomBooking(bookings: BookingApi[]) {
  return bookings.some(isCurrentRoomBooking)
}

export function isCurrentRoomBooking(booking: BookingApi) {
  return currentRoomBookingStates.has(booking.state)
}
