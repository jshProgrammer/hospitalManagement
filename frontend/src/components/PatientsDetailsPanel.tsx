import { useEffect, useState } from 'react'
import Button from './Button'
import LoadingIcon from './LoadingIcon'
import ErrorComponent from './ErrorComponent'
import type { DiagnosisApi } from '../types/Diagnosis'
import type { BookingApi } from '../types/Bookings'

type ActiveTab = 'diagnoses' | 'bookings'

type PatientDetailsPanelProps = {
  diagnoses: DiagnosisApi[]
  bookings: BookingApi[]
  loading: boolean
  error: string | null
  onRetry: () => void
  onClose: () => void
}

export default function PatientDetailsPanel({
  diagnoses,
  bookings,
  loading,
  error,
  onRetry,
  onClose,
}: PatientDetailsPanelProps) {
  const [activeTab, setActiveTab] = useState<ActiveTab>('diagnoses')

  useEffect(() => {
    setActiveTab('diagnoses')
  }, [diagnoses, bookings])

  return (
    <div className="flex h-full flex-col">
      <div className="border-border flex items-center justify-between border-b px-4 py-3">
        <h2 className="text-dark font-semibold">Patient Details</h2>

        <Button label="×" variant="secondary" onClick={onClose} className="px-3" />
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
      </div>

      <div className="min-h-0 flex-1 overflow-auto p-4">
        {loading && <LoadingIcon />}

        {error && !loading && <ErrorComponent message={error} onRetry={onRetry} />}

        {!loading && !error && activeTab === 'diagnoses' && (
          <div className="space-y-3">
            {diagnoses.length === 0 && (
              <p className="text-muted text-sm">Keine Diagnosen vorhanden.</p>
            )}

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
            {bookings.length === 0 && (
              <p className="text-muted text-sm">Keine Bookings vorhanden.</p>
            )}

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
      </div>
    </div>
  )
}