import { requestJson } from './http'
import type {
  DiagnosisApi,
  DiagnosisCreateRequest,
  DoseApi,
  DoseCreateRequest,
  MedicationApi,
  MedicationCreateRequest,
} from '../types/Diagnosis'

export function createDose(payload: DoseCreateRequest) {
  return requestJson<DoseApi>('/api/doses', {
    method: 'POST',
    body: payload,
  })
}

export function createMedication(payload: MedicationCreateRequest) {
  return requestJson<MedicationApi>('/api/medications', {
    method: 'POST',
    body: payload,
  })
}

export function createDiagnosis(payload: DiagnosisCreateRequest) {
  return requestJson<DiagnosisApi>('/api/diagnoses', {
    method: 'POST',
    body: payload,
  })
}

export function terminateDiagnosis(diagnosisId: number) {
  return requestJson<DiagnosisApi>(`/api/diagnoses/${diagnosisId}/terminate`, {
    method: 'POST',
  })
}
