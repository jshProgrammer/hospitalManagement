import type { PatientApi } from './Patient'
import type { DoctorApi } from './Doctor.tsx'
import type { DrugApi } from './Drug.tsx'

export type DoseApi = {
  id: number
  unit: string
  amount: number
  frequency: string
  frequencyAmount: number
}

export type MedicationApi = {
  id: number
  dose: DoseApi
  drug: DrugApi
  started: string
  ended: string | null
}

export type DiagnosisApi = {
  id: number
  disease: string
  medication: MedicationApi | null
  diagnosedBy: DoctorApi
  diagnosedPatient: PatientApi
  diagnosedAt: string
  diagnosedEnd: string | null
}

export type DiagnosesResponse = {
  diagnoses: DiagnosisApi[]
}

export type MedicationCreateRequest = {
  dose_id: number
  drug_id: number
  started: string | null
  ended: string | null
}

export type DiagnosisCreateRequest = {
  disease: string
  medication_id: number
  diagnosed_by: number
  diagnosed_patient: number
  diagnosed_at: string
}
