import { type FormEvent, useState } from 'react'
import { Plus } from 'lucide-react'
import { createPatient, createPatientFromPerson } from '../api/patientActions'
import { ApiRequestError, type ApiFieldErrors } from '../api/http'
import { genderOptions } from '../constants/filters'
import type { PatientCreateRequest, PersonSearchResult } from '../types/Patient'
import ActionFeedback from './ActionFeedback'
import Button from './Button'
import FormField from './FormField'
import Modal from './Modal'

type PatientCreateModalProps = {
  onClose: () => void
  onCreated: () => void
}

type FormState = PatientCreateRequest

const initialFormState: FormState = {
  gender: '',
  firstName: '',
  lastName: '',
  email: '',
  phoneNumber: '',
  plz: '',
  city: '',
  street: '',
  houseNumber: '',
  country: 'Germany',
  birthday: '',
}

export default function PatientCreateModal({ onClose, onCreated }: PatientCreateModalProps) {
  const [form, setForm] = useState<FormState>(initialFormState)
  const [potentialMatches, setPotentialMatches] = useState<PersonSearchResult[]>([])
  const [submitting, setSubmitting] = useState(false)
  const [creatingFromPersonId, setCreatingFromPersonId] = useState<number | null>(null)
  const [fieldErrors, setFieldErrors] = useState<ApiFieldErrors>({})
  const [message, setMessage] = useState<{ type: 'success' | 'error'; text: string } | null>(null)

  function updateField(name: keyof FormState, value: string) {
    setForm(current => ({ ...current, [name]: value }))
    setFieldErrors(current => {
      if (!current[name]) {
        return current
      }

      const next = { ...current }
      delete next[name]
      return next
    })
  }

  async function submitPatient(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setSubmitting(true)
    setMessage(null)
    setFieldErrors({})
    setPotentialMatches([])

    try {
      const response = await createPatient(form)

      if (response.potentialMatches?.length) {
        setPotentialMatches(response.potentialMatches)
        setMessage({
          type: 'error',
          text: 'Possible existing people were found. Review them before creating a new patient.',
        })
        return
      }

      setMessage({ type: 'success', text: 'Patient created.' })
      setForm(initialFormState)
      onCreated()
    } catch (error) {
      handleError(error)
    } finally {
      setSubmitting(false)
    }
  }

  async function createFromExistingPerson(personId: number) {
    setCreatingFromPersonId(personId)
    setMessage(null)

    try {
      await createPatientFromPerson(personId)
      setMessage({ type: 'success', text: 'Patient created from existing person.' })
      setPotentialMatches([])
      onCreated()
    } catch {
      setMessage({ type: 'error', text: 'Could not create a patient from this person.' })
    } finally {
      setCreatingFromPersonId(null)
    }
  }

  function handleError(error: unknown) {
    if (error instanceof ApiRequestError) {
      setFieldErrors(error.fields ?? {})
      setMessage({ type: 'error', text: error.message })
      return
    }

    setMessage({ type: 'error', text: 'Could not create patient.' })
  }

  return (
    <Modal title="Add Patient" onClose={onClose}>
      <form onSubmit={submitPatient} className="min-h-0 flex-1 overflow-auto p-5">
        <div className="space-y-4">
          {message && <ActionFeedback type={message.type} message={message.text} />}

          <div className="grid grid-cols-1 gap-3 md:grid-cols-2">
            <FormField
              label="Gender"
              type="select"
              value={form.gender}
              onChange={event => updateField('gender', event.target.value)}
              options={genderOptions}
              required
              disabled={submitting}
              error={fieldErrors.gender ? 'Invalid gender.' : undefined}
            />
            <FormField
              label="First name"
              value={form.firstName}
              onChange={event => updateField('firstName', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.firstName ? 'Invalid first name.' : undefined}
            />
            <FormField
              label="Last name"
              value={form.lastName}
              onChange={event => updateField('lastName', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.lastName ? 'Invalid last name.' : undefined}
            />
            <FormField
              label="Birthday"
              type="date"
              value={form.birthday}
              onChange={event => updateField('birthday', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.birthday ? 'Use a past date.' : undefined}
            />
            <FormField
              label="Email"
              type="email"
              value={form.email}
              onChange={event => updateField('email', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.email ? 'Invalid email.' : undefined}
            />
            <FormField
              label="Phone"
              type="tel"
              value={form.phoneNumber}
              onChange={event => updateField('phoneNumber', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.phoneNumber ? 'Invalid phone number.' : undefined}
            />
            <FormField
              label="Postal code"
              value={form.plz}
              onChange={event => updateField('plz', event.target.value)}
              pattern="[0-9]{5}"
              required
              disabled={submitting}
              error={fieldErrors.plz ? 'Use exactly 5 digits.' : undefined}
            />
            <FormField
              label="City"
              value={form.city}
              onChange={event => updateField('city', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.city ? 'Invalid city.' : undefined}
            />
            <FormField
              label="Street"
              value={form.street}
              onChange={event => updateField('street', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.street ? 'Invalid street.' : undefined}
            />
            <FormField
              label="House number"
              value={form.houseNumber}
              onChange={event => updateField('houseNumber', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.houseNumber ? 'Invalid house number.' : undefined}
            />
            <FormField
              label="Country"
              value={form.country}
              onChange={event => updateField('country', event.target.value)}
              required
              disabled={submitting}
              error={fieldErrors.country ? 'Invalid country.' : undefined}
            />
          </div>

          {potentialMatches.length > 0 && (
            <div className="border-border bg-surface rounded-lg border p-3">
              <h3 className="text-dark text-sm font-semibold">Possible Matches</h3>
              <div className="mt-3 space-y-2">
                {potentialMatches.map(match => (
                  <div
                    key={match.id}
                    className="border-border bg-elevated flex flex-col gap-3 rounded-md border p-3"
                  >
                    <div>
                      <p className="text-dark text-sm font-semibold">
                        {match.firstName} {match.lastName}
                      </p>
                      <p className="text-muted text-xs">Birthday: {match.birthday}</p>
                      <p className="text-muted text-xs">
                        {match.isPatient ? 'Already a patient' : 'Not a patient'}
                        {match.isEmployee ? ' · Employee' : ''}
                      </p>
                    </div>
                    <Button
                      label={match.isPatient ? 'Already patient' : 'Use this person'}
                      variant="secondary"
                      disabled={match.isPatient || creatingFromPersonId !== null}
                      onClick={() => void createFromExistingPerson(match.id)}
                    />
                  </div>
                ))}
              </div>
            </div>
          )}

          <div className="border-border flex justify-end gap-2 border-t pt-4">
            <Button label="Cancel" variant="secondary" onClick={onClose} disabled={submitting} />
            <Button
              label={submitting ? 'Creating...' : 'Create patient'}
              type="submit"
              variant="primary"
              icon={<Plus className="size-4" />}
              disabled={submitting}
            />
          </div>
        </div>
      </form>
    </Modal>
  )
}
