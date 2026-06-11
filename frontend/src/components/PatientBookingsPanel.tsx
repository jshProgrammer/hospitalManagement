import { type FormEvent, useMemo, useState } from 'react'
import { Bed, LogOut, MoveRight } from 'lucide-react'
import {
  completeBooking,
  createBooking,
  relocateBooking,
} from '../api/patientActions'
import { ApiRequestError } from '../api/http'
import type { BookingApi, BookingCreateRequest, BookingState, RoomApi } from '../types/Bookings'
import { useRoomOptions } from '../hooks/useRoomOptions'
import { isCurrentRoomBooking } from '../utils/bookings'
import ActionFeedback from './ActionFeedback'
import Button from './Button'
import FormField from './FormField'
import SearchableSelect from './SearchableSelect'

type PatientBookingsPanelProps = {
  patientId: number
  bookings: BookingApi[]
  onBookingChanged: (booking: BookingApi) => void
}

type Feedback = {
  type: 'success' | 'error'
  message: string
}

type BookingFormState = {
  from: string
  until: string
  state: BookingState
  roomId: string
}

const initialBookingForm: BookingFormState = {
  from: '',
  until: '',
  state: 'CONFIRMED',
  roomId: '',
}

const bookingStateOptions: { label: string; value: BookingState }[] = [
  { label: 'Pending', value: 'PENDING' },
  { label: 'Confirmed', value: 'CONFIRMED' },
  { label: 'Checked in', value: 'CHECKED_IN' },
]

export default function PatientBookingsPanel({
  patientId,
  bookings,
  onBookingChanged,
}: PatientBookingsPanelProps) {
  const [bookingForm, setBookingForm] = useState(initialBookingForm)
  const [bookingRoomQuery, setBookingRoomQuery] = useState('')
  const [relocatingBookingId, setRelocatingBookingId] = useState<number | null>(null)
  const [relocateRoomId, setRelocateRoomId] = useState('')
  const [relocateRoomQuery, setRelocateRoomQuery] = useState('')
  const [pendingAction, setPendingAction] = useState<string | null>(null)
  const [feedback, setFeedback] = useState<Feedback | null>(null)
  const bookingRoomOptions = useRoomOptions(bookingRoomQuery)
  const relocateRoomOptions = useRoomOptions(relocateRoomQuery)

  const busy = pendingAction !== null
  const bookingRooms = useMemo(
    () => bookingRoomOptions.rooms.map(roomToSelectOption),
    [bookingRoomOptions.rooms]
  )
  const relocationRooms = useMemo(
    () => relocateRoomOptions.rooms.map(roomToSelectOption),
    [relocateRoomOptions.rooms]
  )

  function updateBookingField<Key extends keyof BookingFormState>(
    name: Key,
    value: BookingFormState[Key]
  ) {
    setBookingForm(current => ({ ...current, [name]: value }))
  }

  async function submitBooking(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (!bookingForm.roomId) {
      setFeedback({ type: 'error', message: 'Choose a room from the list.' })
      return
    }

    setPendingAction('create')
    setFeedback(null)

    const payload: BookingCreateRequest = {
      from: bookingForm.from,
      until: bookingForm.until || null,
      state: bookingForm.state,
      room_id: Number(bookingForm.roomId),
      patient_id: patientId,
    }

    try {
      const booking = await createBooking(payload)
      setBookingForm(initialBookingForm)
      setBookingRoomQuery('')
      setFeedback({ type: 'success', message: 'Booking created.' })
      onBookingChanged(booking)
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not create booking.') })
    } finally {
      setPendingAction(null)
    }
  }

  async function submitRelocation(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (relocatingBookingId === null || !relocateRoomId) {
      setFeedback({ type: 'error', message: 'Choose a room from the list.' })
      return
    }

    setPendingAction(`relocate-${relocatingBookingId}`)
    setFeedback(null)

    try {
      const booking = await relocateBooking(relocatingBookingId, { room_id: Number(relocateRoomId) })
      setRelocatingBookingId(null)
      setRelocateRoomId('')
      setRelocateRoomQuery('')
      setFeedback({ type: 'success', message: 'Room changed.' })
      onBookingChanged(booking)
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not change room.') })
    } finally {
      setPendingAction(null)
    }
  }

  async function endBooking(bookingId: number) {
    setPendingAction(`complete-${bookingId}`)
    setFeedback(null)

    try {
      const booking = await completeBooking(bookingId)
      setFeedback({ type: 'success', message: 'Booking ended.' })
      onBookingChanged(booking)
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not end booking.') })
    } finally {
      setPendingAction(null)
    }
  }

  return (
    <div className="space-y-3">
      {feedback && <ActionFeedback type={feedback.type} message={feedback.message} />}
      {bookingRoomOptions.error && <ActionFeedback type="error" message={bookingRoomOptions.error} />}
      {relocateRoomOptions.error && (
        <ActionFeedback type="error" message={relocateRoomOptions.error} />
      )}

      <form onSubmit={submitBooking} className="border-border bg-surface rounded-lg border p-3">
        <div className="space-y-3">
          <h3 className="text-dark text-sm font-semibold">Create Booking</h3>

          <SearchableSelect
            label="Room"
            value={bookingForm.roomId}
            onChange={value => updateBookingField('roomId', value)}
            query={bookingRoomQuery}
            onQueryChange={setBookingRoomQuery}
            options={bookingRooms}
            placeholder="Type room number, floor, or station"
            emptyMessage="No rooms found"
            loading={bookingRoomOptions.loading}
            required
            disabled={busy}
          />
          <FormField
            label="From"
            type="date"
            value={bookingForm.from}
            onChange={event => updateBookingField('from', event.target.value)}
            required
            disabled={busy}
          />
          <FormField
            label="Until"
            type="date"
            value={bookingForm.until}
            onChange={event => updateBookingField('until', event.target.value)}
            disabled={busy}
          />
          <FormField
            label="State"
            type="select"
            value={bookingForm.state}
            onChange={event => updateBookingField('state', event.target.value as BookingState)}
            options={bookingStateOptions}
            required
            disabled={busy}
          />

          <div className="flex justify-end">
            <Button
              label={pendingAction === 'create' ? 'Creating...' : 'Create booking'}
              type="submit"
              variant="primary"
              icon={<Bed className="size-4" />}
              disabled={busy}
            />
          </div>
        </div>
      </form>

      {bookings.length === 0 && <p className="text-muted text-sm">No bookings available</p>}

      {bookings.map(booking => {
        const canChangeBooking = isCurrentRoomBooking(booking)

        return (
          <div key={booking.id} className="border-border bg-surface rounded-lg border p-3">
            <p className="text-dark font-semibold">Room {booking.room.number}</p>
            <p className="text-muted text-sm">State: {booking.state}</p>
            <p className="text-muted text-sm">From: {booking.from}</p>
            <p className="text-muted text-sm">Until: {booking.until ?? '—'}</p>
            <p className="text-muted text-sm">
              Floor {booking.room.floor}, {booking.room.beds} beds
            </p>
            <p className="text-muted text-sm">
              Station: {booking.room.station.name} ({booking.room.station.department.name})
            </p>

            {relocatingBookingId === booking.id && (
              <form onSubmit={submitRelocation} className="mt-3 space-y-3">
                <SearchableSelect
                  label="New room"
                  value={relocateRoomId}
                  onChange={setRelocateRoomId}
                  query={relocateRoomQuery}
                  onQueryChange={setRelocateRoomQuery}
                  options={relocationRooms}
                  placeholder="Type room number, floor, or station"
                  emptyMessage="No rooms found"
                  loading={relocateRoomOptions.loading}
                  required
                  disabled={busy}
                />
                <div className="flex justify-end gap-2">
                  <Button
                    label="Cancel"
                    variant="secondary"
                    onClick={() => {
                      setRelocatingBookingId(null)
                      setRelocateRoomId('')
                      setRelocateRoomQuery('')
                    }}
                    disabled={busy}
                  />
                  <Button
                    label={
                      pendingAction === `relocate-${booking.id}` ? 'Changing...' : 'Change room'
                    }
                    type="submit"
                    variant="primary"
                    icon={<MoveRight className="size-4" />}
                    disabled={busy}
                  />
                </div>
              </form>
            )}

            {relocatingBookingId !== booking.id && (
              <div className="mt-3 flex flex-wrap justify-end gap-2">
                <Button
                  label="Change room"
                  variant="primary"
                  icon={<MoveRight className="size-4" />}
                  onClick={() => setRelocatingBookingId(booking.id)}
                  disabled={busy || !canChangeBooking}
                />
                <Button
                  label={pendingAction === `complete-${booking.id}` ? 'Ending...' : 'End booking'}
                  variant="secondary"
                  icon={<LogOut className="size-4" />}
                  onClick={() => void endBooking(booking.id)}
                  disabled={busy || !canChangeBooking}
                />
              </div>
            )}
          </div>
        )
      })}
    </div>
  )
}

function roomToSelectOption(room: RoomApi) {
  const station = room.station
  const department = station.department

  return {
    label: `Room ${room.number}`,
    value: String(room.id),
    description: `Floor ${room.floor} - ${station.name}, ${department.name} (${department.building}) - ${room.beds} beds`,
    searchText: `${room.number} floor ${room.floor} ${station.name} ${department.name} ${department.building} ${room.beds} beds`,
  }
}

function getActionError(error: unknown, fallback: string) {
  if (error instanceof ApiRequestError) {
    return error.message
  }

  return fallback
}
