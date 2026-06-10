import { useEffect, useMemo, useState } from 'react'
import { requestJson } from '../api/http'
import type { Patient } from '../types/Patient'
import type { BookingsResponse } from '../types/Bookings'
import { hasCurrentRoomBooking } from '../utils/bookings'

type CurrentRoomFilterState = {
  patientIds: Set<number>
  loading: boolean
  error: string | null
}

const initialState: CurrentRoomFilterState = {
  patientIds: new Set(),
  loading: false,
  error: null,
}

export function useCurrentRoomPatientFilter(patients: Patient[], enabled: boolean) {
  const [state, setState] = useState<CurrentRoomFilterState>(initialState)

  useEffect(() => {
    if (!enabled || patients.length === 0) {
      return
    }

    const controller = new AbortController()

    async function loadCurrentRoomPatients() {
      setState(current => ({ ...current, loading: true, error: null }))

      try {
        const results = await Promise.all(
          patients.map(async patient => {
            const response = await requestJson<BookingsResponse>(
              `/api/patients/${patient.id}/bookings?limit=50`,
              { signal: controller.signal }
            )

            return {
              patientId: patient.id,
              hasCurrentBooking: hasCurrentRoomBooking(response.bookings),
            }
          })
        )

        if (controller.signal.aborted) {
          return
        }

        setState({
          patientIds: new Set(
            results.filter(result => result.hasCurrentBooking).map(result => result.patientId)
          ),
          loading: false,
          error: null,
        })
      } catch (error) {
        if (controller.signal.aborted) {
          return
        }

        setState({
          patientIds: new Set(),
          loading: false,
          error: error instanceof Error ? error.message : 'Could not filter current room patients.',
        })
      }
    }

    void loadCurrentRoomPatients()

    return () => controller.abort()
  }, [enabled, patients])

  const data = useMemo(() => {
    if (!enabled) {
      return patients
    }

    return patients.filter(patient => state.patientIds.has(patient.id))
  }, [enabled, patients, state.patientIds])

  return {
    data: enabled ? data : patients,
    loading: enabled && patients.length > 0 ? state.loading : false,
    error: enabled && patients.length > 0 ? state.error : null,
  }
}
