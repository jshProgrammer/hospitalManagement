import { useState } from 'react'
import { ClipboardPlus, Pill, X } from 'lucide-react'
import Button from './Button'
import LoadingIcon from './LoadingIcon'
import ErrorComponent from './ErrorComponent'
import type { DiagnosisApi } from '../types/Diagnosis'
import type { BookingApi } from '../types/Bookings'
import type { BookingChangeOptions } from '../hooks/usePatientDetails'
import PatientBookingsPanel from './PatientBookingsPanel'
import PatientDiagnosisModal from './PatientDiagnosisModal'
import { terminateDiagnosis } from '../api/medicationActions'
import ActionFeedback from './ActionFeedback'
import Modal from './Modal'

type ActiveTab = 'diagnoses' | 'bookings'

type PatientDetailsPanelProps = {
  patientId: number
  diagnoses: DiagnosisApi[]
  bookings: BookingApi[]
  loading: boolean
  error: string | null
  onRetry: () => void
  onClose: () => void
  onActionCompleted: (booking?: BookingApi, options?: BookingChangeOptions) => void
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
  const [pendingDiagnosisId, setPendingDiagnosisId] = useState<number | null>(null)
  const [diagnosisActionError, setDiagnosisActionError] = useState<string | null>(null)
  const [selectedDiagnosis, setSelectedDiagnosis] = useState<DiagnosisApi | null>(null)
  const [showDiagnosisModal, setShowDiagnosisModal] = useState(false)

  async function handleTerminateDiagnosis(diagnosisId: number) {
    setPendingDiagnosisId(diagnosisId)
    setDiagnosisActionError(null)

    try {
      await terminateDiagnosis(diagnosisId)
      onActionCompleted()
    } catch {
      setDiagnosisActionError('Could not terminate diagnosis.')
    } finally {
      setPendingDiagnosisId(null)
    }
  }

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
      </div>

      <div className="min-h-0 flex-1 overflow-auto p-4">
        {loading && <LoadingIcon />}

        {error && !loading && <ErrorComponent message={error} onRetry={onRetry} />}

        {!loading && !error && activeTab === 'diagnoses' && (
          <div className="space-y-3">
            {diagnosisActionError && (
              <ActionFeedback type="error" message={diagnosisActionError} />
            )}

            <div className="flex justify-end">
              <Button
                label="Add diagnosis"
                variant="primary"
                icon={<ClipboardPlus className="size-4" />}
                onClick={() => setShowDiagnosisModal(true)}
              />
            </div>

            {diagnoses.length === 0 && <p className="text-muted text-sm">No diagnoses available</p>}

            {diagnoses.map(diagnosis => (
              <div
                key={diagnosis.id}
                role="button"
                tabIndex={0}
                onClick={() => setSelectedDiagnosis(diagnosis)}
                onKeyDown={event => {
                  if (event.key === 'Enter' || event.key === ' ') {
                    event.preventDefault()
                    setSelectedDiagnosis(diagnosis)
                  }
                }}
                className="border-border bg-surface hover:border-accent focus:border-accent focus:ring-accent/20 cursor-pointer rounded-lg border p-3 text-left transition outline-none focus:ring-2"
              >
                <div className="flex items-start justify-between gap-3">
                  <div>
                    <p className="text-dark font-semibold">{diagnosis.disease}</p>
                    <p className="text-muted text-sm">Diagnosed at: {diagnosis.diagnosedAt}</p>
                    <p className="text-muted text-sm">End: {diagnosis.diagnosedEnd ?? '—'}</p>
                    <p className="text-muted text-sm">
                      Doctor: {diagnosis.diagnosedBy.employee.person.firstName}{' '}
                      {diagnosis.diagnosedBy.employee.person.lastName}
                    </p>
                  </div>
                  <Pill className="text-accent mt-1 size-4 shrink-0" />
                </div>
                <div className="mt-3 flex justify-end" onClick={event => event.stopPropagation()}>
                  <Button
                    label={
                      diagnosis.diagnosedEnd
                        ? 'Terminated'
                        : pendingDiagnosisId === diagnosis.id
                          ? 'Terminating...'
                          : 'Terminate'
                    }
                    variant="secondary"
                    disabled={Boolean(diagnosis.diagnosedEnd) || pendingDiagnosisId !== null}
                    onClick={() => void handleTerminateDiagnosis(diagnosis.id)}
                  />
                </div>
              </div>
            ))}
          </div>
        )}

        {!loading && !error && activeTab === 'bookings' && (
          <PatientBookingsPanel
            patientId={patientId}
            bookings={bookings}
            onBookingChanged={booking => onActionCompleted(booking)}
          />
        )}
      </div>

      {selectedDiagnosis && (
        <DiagnosisTreatmentModal
          diagnosis={selectedDiagnosis}
          onClose={() => setSelectedDiagnosis(null)}
        />
      )}

      {showDiagnosisModal && (
        <PatientDiagnosisModal
          patientId={patientId}
          onClose={() => setShowDiagnosisModal(false)}
          onCreated={onActionCompleted}
        />
      )}
    </div>
  )
}

function DiagnosisTreatmentModal({
  diagnosis,
  onClose,
}: {
  diagnosis: DiagnosisApi
  onClose: () => void
}) {
  const medication = diagnosis.medication
  const dose = medication?.dose
  const drug = medication?.drug

  return (
    <Modal title={diagnosis.disease} onClose={onClose} widthClassName="max-w-2xl">
      <div className="space-y-4 overflow-auto p-5">
        <section className="grid grid-cols-1 gap-3 md:grid-cols-2">
          <DetailItem label="Diagnosed at" value={diagnosis.diagnosedAt} />
          <DetailItem label="Diagnosis end" value={diagnosis.diagnosedEnd ?? 'Active'} />
          <DetailItem
            label="Doctor"
            value={`${diagnosis.diagnosedBy.employee.person.firstName} ${diagnosis.diagnosedBy.employee.person.lastName}`}
          />
          <DetailItem label="Doctor type" value={formatEnumValue(diagnosis.diagnosedBy.type)} />
        </section>

        {medication && dose && drug ? (
          <>
            <section className="border-border rounded-lg border p-3">
              <h3 className="text-dark text-sm font-semibold">Medicine</h3>
              <div className="mt-3 grid grid-cols-1 gap-3 md:grid-cols-2">
                <DetailItem label="Drug" value={drug.name} />
                <DetailItem label="Type" value={formatEnumValue(drug.type)} />
                <DetailItem label="Active ingredient" value={drug.activeIngredient} />
                <DetailItem label="Stock" value={String(drug.stock)} />
              </div>
            </section>

            <section className="border-border rounded-lg border p-3">
              <h3 className="text-dark text-sm font-semibold">Treatment</h3>
              <p className="text-dark mt-2 text-sm font-medium">
                Give {dose.amount} {formatDoseUnit(dose.unit, dose.amount)}{' '}
                {formatDoseFrequency(dose.frequency, dose.frequencyAmount)}.
              </p>
              <div className="mt-3 grid grid-cols-1 gap-3 md:grid-cols-2">
                <DetailItem label="Dose amount" value={`${dose.amount} ${formatDoseUnit(dose.unit, dose.amount)}`} />
                <DetailItem label="Frequency" value={formatDoseFrequency(dose.frequency, dose.frequencyAmount)} />
                <DetailItem label="Medication started" value={medication.started ?? '—'} />
                <DetailItem label="Medication ended" value={medication.ended ?? 'Ongoing'} />
              </div>
            </section>
          </>
        ) : (
          <ActionFeedback type="error" message="No medication is linked to this diagnosis." />
        )}

        <div className="border-border flex justify-end border-t pt-4">
          <Button label="Close" variant="secondary" onClick={onClose} />
        </div>
      </div>
    </Modal>
  )
}

function DetailItem({ label, value }: { label: string; value: string }) {
  return (
    <div>
      <p className="text-muted text-xs font-semibold">{label}</p>
      <p className="text-dark text-sm">{value}</p>
    </div>
  )
}

function formatEnumValue(value: string) {
  return value
    .toLowerCase()
    .replaceAll('_', ' ')
    .replace(/\b\w/g, char => char.toUpperCase())
}

function formatDoseUnit(unit: string, amount: number) {
  const label = formatEnumValue(unit).toLowerCase()
  return amount === 1 ? label : `${label}s`
}

function formatDoseFrequency(frequency: string, frequencyAmount: number) {
  switch (frequency) {
    case 'EVERY_X_DAYS':
      return `every ${frequencyAmount} ${frequencyAmount === 1 ? 'day' : 'days'}`
    case 'X_DAILY':
      return `${frequencyAmount} ${frequencyAmount === 1 ? 'time' : 'times'} daily`
    case 'EVERY_X_HOURS':
      return `every ${frequencyAmount} ${frequencyAmount === 1 ? 'hour' : 'hours'}`
    case 'X_WEEKLY':
      return `${frequencyAmount} ${frequencyAmount === 1 ? 'time' : 'times'} weekly`
    case 'EVERY_X_WEEKS':
      return `every ${frequencyAmount} ${frequencyAmount === 1 ? 'week' : 'weeks'}`
    default:
      return formatEnumValue(frequency).toLowerCase()
  }
}
