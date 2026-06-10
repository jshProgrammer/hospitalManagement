import { useEffect, useState } from 'react'
import { requestJson } from '../api/http'
import type { DoctorApi } from '../types/Doctor'
import type { DoseApi } from '../types/Diagnosis'
import type { DrugApi } from '../types/Drug'

type PageResponse<T> = {
  content: T[]
}

type DoctorsResponse = {
  doctors: DoctorApi[]
}

type MedicationOptionsState = {
  doctors: DoctorApi[]
  doses: DoseApi[]
  drugs: DrugApi[]
  loading: boolean
  error: string | null
}

const initialState: MedicationOptionsState = {
  doctors: [],
  doses: [],
  drugs: [],
  loading: true,
  error: null,
}

export function useMedicationOptions() {
  const [state, setState] = useState<MedicationOptionsState>(initialState)

  useEffect(() => {
    const controller = new AbortController()

    async function loadOptions() {
      try {
        setState(current => ({ ...current, loading: true, error: null }))

        const [doctorsResponse, dosesResponse, drugsResponse] = await Promise.all([
          requestJson<DoctorsResponse>('/api/doctors?limit=100', { signal: controller.signal }),
          requestJson<PageResponse<DoseApi>>('/api/doses?page=0&size=100&sort=id,asc', {
            signal: controller.signal,
          }),
          requestJson<PageResponse<DrugApi>>('/api/drugs?page=0&size=100&sort=name,asc', {
            signal: controller.signal,
          }),
        ])

        if (controller.signal.aborted) {
          return
        }

        setState({
          doctors: doctorsResponse.doctors,
          doses: dosesResponse.content,
          drugs: drugsResponse.content,
          loading: false,
          error: null,
        })
      } catch (error) {
        if (controller.signal.aborted) {
          return
        }

        setState({
          doctors: [],
          doses: [],
          drugs: [],
          loading: false,
          error: error instanceof Error ? error.message : 'Could not load medication options.',
        })
      }
    }

    void loadOptions()

    return () => controller.abort()
  }, [])

  return state
}
