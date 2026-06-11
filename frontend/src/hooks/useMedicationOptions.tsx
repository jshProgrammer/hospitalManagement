import { useEffect, useState } from 'react'
import { requestJson } from '../api/http'
import type { DoctorApi } from '../types/Doctor'
import type { DrugApi } from '../types/Drug'

type PageResponse<T> = {
  content: T[]
}

type DoctorsResponse = {
  doctors: DoctorApi[]
}

type MedicationOptionsState = {
  doctors: DoctorApi[]
  drugs: DrugApi[]
  doctorsLoading: boolean
  drugsLoading: boolean
  error: string | null
}

const initialState: MedicationOptionsState = {
  doctors: [],
  drugs: [],
  doctorsLoading: true,
  drugsLoading: true,
  error: null,
}

export function useMedicationOptions(drugQuery: string) {
  const [state, setState] = useState<MedicationOptionsState>(initialState)
  const debouncedDrugQuery = useDebouncedValue(drugQuery.trim(), 250)

  useEffect(() => {
    const controller = new AbortController()

    async function loadDoctors() {
      try {
        setState(current => ({ ...current, doctorsLoading: true, error: null }))

        const doctorsResponse = await requestJson<DoctorsResponse>('/api/doctors?limit=500', {
          signal: controller.signal,
        })

        if (controller.signal.aborted) {
          return
        }

        setState(current => ({
          ...current,
          doctors: doctorsResponse.doctors,
          doctorsLoading: false,
          error: null,
        }))
      } catch (error) {
        if (controller.signal.aborted) {
          return
        }

        setState(current => ({
          ...current,
          doctors: [],
          doctorsLoading: false,
          error: error instanceof Error ? error.message : 'Could not load medication options.',
        }))
      }
    }

    void loadDoctors()

    return () => controller.abort()
  }, [])

  useEffect(() => {
    const controller = new AbortController()

    async function loadDrugs() {
      try {
        setState(current => ({ ...current, drugsLoading: true, error: null }))

        const params = new URLSearchParams({
          page: '0',
          size: '20',
          sort: 'name,asc',
        })

        if (debouncedDrugQuery) {
          params.set('nameContains', debouncedDrugQuery)
        }

        const drugsResponse = await requestJson<PageResponse<DrugApi>>(`/api/drugs?${params}`, {
          signal: controller.signal,
        })

        if (controller.signal.aborted) {
          return
        }

        setState(current => ({
          ...current,
          drugs: drugsResponse.content,
          drugsLoading: false,
          error: null,
        }))
      } catch (error) {
        if (controller.signal.aborted) {
          return
        }

        setState(current => ({
          ...current,
          drugs: [],
          drugsLoading: false,
          error: error instanceof Error ? error.message : 'Could not load medication options.',
        }))
      }
    }

    void loadDrugs()

    return () => controller.abort()
  }, [debouncedDrugQuery])

  return state
}

function useDebouncedValue(value: string, delayMs: number) {
  const [debouncedValue, setDebouncedValue] = useState(value)

  useEffect(() => {
    const timeout = window.setTimeout(() => setDebouncedValue(value), delayMs)
    return () => window.clearTimeout(timeout)
  }, [delayMs, value])

  return debouncedValue
}
