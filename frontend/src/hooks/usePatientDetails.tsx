import { useCallback, useEffect, useRef, useState } from 'react'
import type { DiagnosesResponse, DiagnosisApi } from '../types/Diagnosis'
import type { BookingApi, BookingsResponse } from '../types/Bookings.ts'

type PatientDetailsState = {
  patientId: number | null
  diagnoses: DiagnosisApi[]
  bookings: BookingApi[]
  loading: boolean
  error: string | null
}

const initialState: PatientDetailsState = {
  patientId: null,
  diagnoses: [],
  bookings: [],
  loading: false,
  error: null,
}

async function fetchJson<T>(url: string, signal: AbortSignal): Promise<T> {
  const response = await fetch(url, { signal })

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status} ${response.statusText}`)
  }

  return (await response.json()) as Promise<T>
}

export function usePatientDetails() {
  const [state, setState] = useState<PatientDetailsState>(initialState)

  const controllerRef = useRef<AbortController | null>(null)
  const requestIdRef = useRef(0)

  const loadPatientDetails = useCallback(async (patientId: number) => {
    controllerRef.current?.abort()

    const controller = new AbortController()
    const requestId = ++requestIdRef.current

    controllerRef.current = controller

    setState({
      patientId,
      diagnoses: [],
      bookings: [],
      loading: true,
      error: null,
    })

    try {
      const [diagnosesResponse, bookingsResponse] = await Promise.all([
        fetchJson<DiagnosesResponse>(`/api/patients/${patientId}/diagnoses`, controller.signal),
        fetchJson<BookingsResponse>(`/api/patients/${patientId}/bookings`, controller.signal),
      ])

      if (controller.signal.aborted || requestId !== requestIdRef.current) {
        return null
      }

      const details = {
        diagnoses: diagnosesResponse.diagnoses,
        bookings: bookingsResponse.bookings,
      }

      setState({
        patientId,
        ...details,
        loading: false,
        error: null,
      })

      return details
    } catch (error) {
      if (controller.signal.aborted || requestId !== requestIdRef.current) {
        return null
      }

      setState({
        patientId,
        diagnoses: [],
        bookings: [],
        loading: false,
        error: error instanceof Error ? error.message : 'Error Loading Patient Details',
      })

      return null
    }
  }, [])

  const clearPatientDetails = useCallback(() => {
    controllerRef.current?.abort()
    requestIdRef.current += 1
    setState(initialState)
  }, [])

  const reloadPatientDetails = useCallback(() => {
    if (state.patientId === null) {
      return
    }

    void loadPatientDetails(state.patientId)
  }, [loadPatientDetails, state.patientId])

  useEffect(() => {
    return () => {
      controllerRef.current?.abort()
    }
  }, [])

  return {
    ...state,
    loadPatientDetails,
    reloadPatientDetails,
    clearPatientDetails,
  }
}