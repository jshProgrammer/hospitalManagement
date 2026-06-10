import { type FormEvent, useState } from 'react'
import { Bed, ClipboardPlus, LogOut, MoveRight } from 'lucide-react'
import { createBooking, dischargePatient, relocatePatient } from '../api/patientActions'
import { ApiRequestError } from '../api/http'
import type { BookingCreateRequest, BookingState } from '../types/Bookings'
import ActionFeedback from './ActionFeedback'
import Button from './Button'
import FormField from './FormField'
import PatientDiagnosisModal from './PatientDiagnosisModal'

type PatientActionsPanelProps = {
  patientId: number
  hasCurrentBooking: boolean
  onCompleted: () => void
}

type Feedback = {
  type: 'success' | 'error'
  message: string
}

const bookingStateOptions: { label: string; value: BookingState }[] = [
  { label: 'Pending', value: 'PENDING' },
  { label: 'Confirmed', value: 'CONFIRMED' },
  { label: 'Checked in', value: 'CHECKED_IN' },
]

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

export default function PatientActionsPanel({
  patientId,
  hasCurrentBooking,
  onCompleted,
}: PatientActionsPanelProps) {
  const [bookingForm, setBookingForm] = useState(initialBookingForm)
  const [showDiagnosisModal, setShowDiagnosisModal] = useState(false)
  const [relocateRoomId, setRelocateRoomId] = useState('')
  const [pendingAction, setPendingAction] = useState<string | null>(null)
  const [feedback, setFeedback] = useState<Feedback | null>(null)

  const busy = pendingAction !== null

  function updateBookingField<Key extends keyof BookingFormState>(
    name: Key,
    value: BookingFormState[Key]
  ) {
    setBookingForm(current => ({ ...current, [name]: value }))
  }

  async function submitBooking(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setPendingAction('booking')
    setFeedback(null)

    const payload: BookingCreateRequest = {
      from: bookingForm.from,
      until: bookingForm.until || null,
      state: bookingForm.state,
      room_id: Number(bookingForm.roomId),
      patient_id: patientId,
    }

    try {
      await createBooking(payload)
      setBookingForm(initialBookingForm)
      setFeedback({ type: 'success', message: 'Booking created.' })
      onCompleted()
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not create booking.') })
    } finally {
      setPendingAction(null)
    }
  }

  async function submitRelocation(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setPendingAction('relocate')
    setFeedback(null)

    try {
      await relocatePatient(patientId, { room_id: Number(relocateRoomId) })
      setRelocateRoomId('')
      setFeedback({ type: 'success', message: 'Patient relocated.' })
      onCompleted()
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not relocate patient.') })
    } finally {
      setPendingAction(null)
    }
  }

  async function handleDischarge() {
    setPendingAction('discharge')
    setFeedback(null)

    try {
      await dischargePatient(patientId)
      setFeedback({ type: 'success', message: 'Patient discharged.' })
      onCompleted()
    } catch (error) {
      setFeedback({ type: 'error', message: getActionError(error, 'Could not discharge patient.') })
    } finally {
      setPendingAction(null)
    }
  }

  return (
    <div className="space-y-4">
      {feedback && <ActionFeedback type={feedback.type} message={feedback.message} />}

      <div className="border-border bg-surface rounded-lg border p-3">
        <div className="flex flex-col gap-3">
          <div>
            <h3 className="text-dark text-sm font-semibold">Add Diagnosis</h3>
          </div>

          <div className="flex justify-end">
            <Button
              label="Add diagnosis"
              variant="primary"
              icon={<ClipboardPlus className="size-4" />}
              onClick={() => setShowDiagnosisModal(true)}
            />
          </div>
        </div>
      </div>

      <form onSubmit={submitBooking} className="border-border bg-surface rounded-lg border p-3">
        <div className="space-y-3">
          <div>
            <h3 className="text-dark text-sm font-semibold">Create Booking</h3>
          </div>

          <FormField
            label="Room ID"
            type="number"
            min="1"
            value={bookingForm.roomId}
            onChange={event => updateBookingField('roomId', event.target.value)}
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
              label={pendingAction === 'booking' ? 'Creating...' : 'Create booking'}
              type="submit"
              variant="primary"
              icon={<Bed className="size-4" />}
              disabled={busy}
            />
          </div>
        </div>
      </form>

      <form onSubmit={submitRelocation} className="border-border bg-surface rounded-lg border p-3">
        <div className="space-y-3">
          <div>
            <h3 className="text-dark text-sm font-semibold">Relocate Patient</h3>
          </div>

          <FormField
            label="New room ID"
            type="number"
            min="1"
            value={relocateRoomId}
            onChange={event => setRelocateRoomId(event.target.value)}
            required
            disabled={busy || !hasCurrentBooking}
          />

          <div className="flex justify-end">
            <Button
              label={
                !hasCurrentBooking
                  ? 'No current booking'
                  : pendingAction === 'relocate'
                    ? 'Moving...'
                    : 'Relocate'
              }
              type="submit"
              variant="primary"
              icon={<MoveRight className="size-4" />}
              disabled={busy || !hasCurrentBooking}
            />
          </div>
        </div>
      </form>

      <div className="border-border bg-surface rounded-lg border p-3">
        <div className="flex flex-col gap-3">
          <div>
            <h3 className="text-dark text-sm font-semibold">Discharge Patient</h3>
          </div>

          <div className="flex justify-end">
            <Button
              label={
                !hasCurrentBooking
                  ? 'No current booking'
                  : pendingAction === 'discharge'
                    ? 'Discharging...'
                    : 'Discharge'
              }
              variant="secondary"
              icon={<LogOut className="size-4" />}
              onClick={() => void handleDischarge()}
              disabled={busy || !hasCurrentBooking}
            />
          </div>
        </div>
      </div>

      {showDiagnosisModal && (
        <PatientDiagnosisModal
          patientId={patientId}
          onClose={() => setShowDiagnosisModal(false)}
          onCreated={onCompleted}
        />
      )}
    </div>
  )
}

function getActionError(error: unknown, fallback: string) {
  if (error instanceof ApiRequestError) {
    return error.message
  }

  return fallback
}
