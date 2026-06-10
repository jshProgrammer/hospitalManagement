import { type FormEvent, useState } from 'react'
import { ClipboardPlus } from 'lucide-react'
import { createDiagnosis, createMedication } from '../api/medicationActions'
import { useMedicationOptions } from '../hooks/useMedicationOptions'
import ActionFeedback from './ActionFeedback'
import Button from './Button'
import FormField from './FormField'
import Modal from './Modal'

type PatientDiagnosisModalProps = {
  patientId: number
  onClose: () => void
  onCreated: () => void
}

const today = new Date().toISOString().slice(0, 10)

const initialForm = {
  diagnosis: '',
  doctorId: '',
  diagnosedAt: today,
  noMedication: false,
  drugId: '',
  doseId: '',
  started: today,
  ended: '',
}

type FormState = typeof initialForm

export default function PatientDiagnosisModal({
  patientId,
  onClose,
  onCreated,
}: PatientDiagnosisModalProps) {
  const options = useMedicationOptions()
  const [form, setForm] = useState<FormState>(initialForm)
  const [submitting, setSubmitting] = useState(false)
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null)

  const busy = submitting || options.loading

  function updateField<Key extends keyof FormState>(name: Key, value: FormState[Key]) {
    setForm(current => ({ ...current, [name]: value }))
  }

  async function submitDiagnosis(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setMessage(null)

    if (form.noMedication) {
      setMessage({
        type: 'error',
        text: 'This backend requires a medication for each diagnosis.',
      })
      return
    }

    setSubmitting(true)

    try {
      const medication = await createMedication({
        dose_id: Number(form.doseId),
        drug_id: Number(form.drugId),
        started: form.started || null,
        ended: form.ended || null,
      })

      await createDiagnosis({
        disease: form.diagnosis,
        medication_id: medication.id,
        diagnosed_by: Number(form.doctorId),
        diagnosed_patient: patientId,
        diagnosed_at: form.diagnosedAt,
      })

      onCreated()
      onClose()
    } catch {
      setMessage({ type: 'error', text: 'Could not add diagnosis.' })
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Modal title="Add Diagnosis" onClose={onClose}>
      <form onSubmit={submitDiagnosis} className="min-h-0 flex-1 overflow-auto p-5">
        <div className="space-y-4">
          {message && <ActionFeedback type={message.type} message={message.text} />}
          {options.error && <ActionFeedback type="error" message={options.error} />}

          <div className="grid grid-cols-1 gap-3 md:grid-cols-2">
            <FormField
              label="Diagnosis"
              value={form.diagnosis}
              onChange={event => updateField('diagnosis', event.target.value)}
              required
              disabled={busy}
            />
            <FormField
              label="Doctor"
              type="select"
              value={form.doctorId}
              onChange={event => updateField('doctorId', event.target.value)}
              options={options.doctors.map(doctor => ({
                label: `${doctor.employee.person.firstName} ${doctor.employee.person.lastName} (${formatEnumValue(doctor.type)})`,
                value: String(doctor.id),
              }))}
              required
              disabled={busy}
            />
            <FormField
              label="Diagnosed at"
              type="date"
              value={form.diagnosedAt}
              onChange={event => updateField('diagnosedAt', event.target.value)}
              required
              disabled={busy}
            />
          </div>

          <label className="text-dark flex items-center gap-2 text-sm">
            <input
              type="checkbox"
              checked={form.noMedication}
              onChange={event => updateField('noMedication', event.target.checked)}
              disabled={submitting}
              className="accent-accent size-4"
            />
            <span>No medication</span>
          </label>

          {!form.noMedication && (
            <div className="border-border bg-surface grid grid-cols-1 gap-3 rounded-lg border p-3 md:grid-cols-2">
              <FormField
                label="Drug"
                type="select"
                value={form.drugId}
                onChange={event => updateField('drugId', event.target.value)}
                options={options.drugs.map(drug => ({
                  label: `${drug.name} (${formatEnumValue(drug.type)}, stock ${drug.stock})`,
                  value: String(drug.id),
                }))}
                required
                disabled={busy}
              />
              <FormField
                label="Dose"
                type="select"
                value={form.doseId}
                onChange={event => updateField('doseId', event.target.value)}
                options={options.doses.map(dose => ({
                  label: formatDoseLabel(dose),
                  value: String(dose.id),
                }))}
                required
                disabled={busy}
              />
              <FormField
                label="Medication started"
                type="date"
                value={form.started}
                onChange={event => updateField('started', event.target.value)}
                required
                disabled={submitting}
              />
              <FormField
                label="Medication ended"
                type="date"
                value={form.ended}
                onChange={event => updateField('ended', event.target.value)}
                disabled={submitting}
              />
            </div>
          )}

          <div className="border-border flex justify-end gap-2 border-t pt-4">
            <Button label="Cancel" variant="secondary" onClick={onClose} disabled={submitting} />
            <Button
              label={submitting ? 'Adding...' : 'Add diagnosis'}
              type="submit"
              variant="primary"
              icon={<ClipboardPlus className="size-4" />}
              disabled={busy}
            />
          </div>
        </div>
      </form>
    </Modal>
  )
}

function formatDoseLabel(dose: {
  amount: number
  unit: string
  frequency: string
  frequencyAmount: number
}) {
  return `${dose.amount} ${dose.unit.toLowerCase()} · ${formatDoseFrequency(
    dose.frequency,
    dose.frequencyAmount
  )}`
}

function formatDoseFrequency(frequency: string, amount: number) {
  switch (frequency.toUpperCase().replaceAll(' ', '_')) {
    case 'EVERY_X_DAYS':
      return `every ${amount} ${amount === 1 ? 'day' : 'days'}`
    case 'EVERY_X_HOURS':
      return `every ${amount} ${amount === 1 ? 'hour' : 'hours'}`
    case 'EVERY_X_WEEKS':
      return `every ${amount} ${amount === 1 ? 'week' : 'weeks'}`
    case 'X_DAILY':
      return `${amount} ${amount === 1 ? 'time' : 'times'} daily`
    case 'X_WEEKLY':
      return `${amount} ${amount === 1 ? 'time' : 'times'} weekly`
    default:
      return formatEnumValue(frequency)
  }
}

function formatEnumValue(value: string) {
  return value
    .toLowerCase()
    .replaceAll('_', ' ')
    .replace(/\b\w/g, char => char.toUpperCase())
}
