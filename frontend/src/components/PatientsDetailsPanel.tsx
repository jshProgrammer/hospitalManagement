import { useState } from 'react'
import { X } from 'lucide-react'
import Button from './Button'
import LoadingIcon from './LoadingIcon'
import ErrorComponent from './ErrorComponent'
import type { DiagnosisApi } from '../types/Diagnosis'
import type { BookingApi } from '../types/Bookings'
import PatientActionsPanel from './PatientActionsPanel'
import { hasCurrentRoomBooking } from '../utils/bookings'

type ActiveTab = 'diagnoses' | 'bookings' | 'actions'

type PatientDetailsPanelProps = {
  patientId: number
  diagnoses: DiagnosisApi[]
  bookings: BookingApi[]
  loading: boolean
  error: string | null
  onRetry: () => void
  onClose: () => void
  onActionCompleted: () => void
}

export default function PatientDetailsPanel({
  patientId,
  diagnoses,
  bookings,
  loading,
  error,
  onRetry,
  onClose,
  onActionCompleted,
}: PatientDetailsPanelProps) {
  const [activeTab, setActiveTab] = useState<ActiveTab>('diagnoses')
  const hasCurrentBooking = hasCurrentRoomBooking(bookings)

  return (
    <div className="flex h-full flex-col">
      <div className="border-border flex items-center justify-between border-b px-4 py-3">
        <h2 className="text-dark font-semibold">Patient Details</h2>

        <Button label="Close" variant="secondary" icon={<X className="size-4" />} onClick={onClose} />
      </div>

      <div className="border-border flex gap-2 border-b px-4 py-3">
        <Button
          label="Diagnosis"
          variant={activeTab === 'diagnoses' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('diagnoses')}
        />

        <Button
          label="Bookings"
          variant={activeTab === 'bookings' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('bookings')}
        />

        <Button
          label="Actions"
          variant={activeTab === 'actions' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('actions')}
        />
      </div>

      <div className="min-h-0 flex-1 overflow-auto p-4">
        {loading && <LoadingIcon />}

        {error && !loading && <ErrorComponent message={error} onRetry={onRetry} />}

        {!loading && !error && activeTab === 'diagnoses' && (
          <div className="space-y-3">
            {diagnoses.length === 0 && <p className="text-muted text-sm">No diagnoses available</p>}

            {diagnoses.map(diagnosis => (
              <div key={diagnosis.id} className="border-border bg-surface rounded-lg border p-3">
                <p className="text-dark font-semibold">{diagnosis.disease}</p>
                <p className="text-muted text-sm">Diagnosed at: {diagnosis.diagnosedAt}</p>
                <p className="text-muted text-sm">End: {diagnosis.diagnosedEnd ?? '—'}</p>
                <p className="text-muted text-sm">
                  Doctor: {diagnosis.diagnosedBy.employee.person.firstName}{' '}
                  {diagnosis.diagnosedBy.employee.person.lastName}
                </p>
              </div>
            ))}
          </div>
        )}

        {!loading && !error && activeTab === 'bookings' && (
          <div className="space-y-3">
            {bookings.length === 0 && <p className="text-muted text-sm">No bookings available</p>}
            {bookings.map(booking => (
              <div key={booking.id} className="border-border bg-surface rounded-lg border p-3">
                <p className="text-dark font-semibold">Room {booking.room.number}</p>
                <p className="text-muted text-sm">State: {booking.state}</p>
                <p className="text-muted text-sm">From: {booking.from}</p>
                <p className="text-muted text-sm">Until: {booking.until}</p>
                <p className="text-muted text-sm">Station: {booking.room.station.name}</p>
              </div>
            ))}
          </div>
        )}

        {!loading && !error && activeTab === 'actions' && (
          <PatientActionsPanel
            patientId={patientId}
            hasCurrentBooking={hasCurrentBooking}
            onCompleted={onActionCompleted}
          />
        )}
      </div>
    </div>
  )
}
