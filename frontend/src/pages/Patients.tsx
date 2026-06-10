import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { useCursorPageData } from '../hooks/useCursorPageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFilters } from '../constants/filters.ts'
import { personColumns } from '../constants/columns.ts'
import { usePatientDetails } from '../hooks/usePatientDetails.tsx'
import PatientDetailsPanel from '../components/PatientsDetailsPanel.tsx'
import PatientCreatePanel from '../components/PatientCreatePanel.tsx'
import Button from '../components/Button.tsx'
import { useState } from 'react'

const columns = [...personColumns] satisfies (keyof Patient)[]
type PanelMode = 'create' | 'details' | null

export function Patients() {
  const [panelMode, setPanelMode] = useState<PanelMode>(null)
  const { filters, setFilters, url } = useTableFilters(`/api/patients`)
  const { data, loading, error, reload, page, hasMore, canGoBack, goToNextPage, goToPreviousPage } =
    useCursorPageData<PatientApi, Patient, 'patients'>(url, 'patients', mapPatient)

  const {
    diagnoses,
    bookings,
    loading: detailsLoading,
    error: detailsError,
    loadPatientDetails,
    reloadPatientDetails,
    clearPatientDetails,
    patientId,
  } = usePatientDetails()

  function handlePatientClick(patient: Patient) {
    setPanelMode('details')
    void loadPatientDetails(patient.id)
  }

  function openCreatePanel() {
    clearPatientDetails()
    setPanelMode('create')
  }

  function closePanel() {
    clearPatientDetails()
    setPanelMode(null)
  }

  function handlePatientCreated() {
    void reload()
  }

  return (
    <MainPage
      title="Patients"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      onRowClick={handlePatientClick}
      getRowKey={patient => patient.id}
      headerActions={<Button label="Add patient" variant="primary" onClick={openCreatePanel} />}
      pagination={{
        type: 'cursor',
        page,
        hasNextPage: hasMore,
        canGoBack,
        onNextPage: goToNextPage,
        onPreviousPage: goToPreviousPage,
      }}
      filters={<TableFilters fields={patientFilters} values={filters} onChange={setFilters} />}
      detailsPanel={
        panelMode === 'create' ? (
          <PatientCreatePanel onClose={closePanel} onCreated={handlePatientCreated} />
        ) : patientId !== null ? (
          <PatientDetailsPanel
            key={patientId}
            diagnoses={diagnoses}
            bookings={bookings}
            loading={detailsLoading}
            error={detailsError}
            onRetry={reloadPatientDetails}
            onClose={closePanel}
          />
        ) : undefined
      }
    />
  )
}
