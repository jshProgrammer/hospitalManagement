import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { useCursorPageData } from '../hooks/useCursorPageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFiltersWithStatus } from '../constants/filters.ts'
import { personColumns } from '../constants/columns.ts'
import { usePatientDetails } from '../hooks/usePatientDetails.tsx'
import PatientDetailsPanel from '../components/PatientsDetailsPanel.tsx'
import PatientCreateModal from '../components/PatientCreateModal.tsx'
import Button from '../components/Button.tsx'
import { useState } from 'react'
import { Plus } from 'lucide-react'

const columns = [...personColumns] satisfies (keyof Patient)[]

export function Patients() {
  const [showCreateModal, setShowCreateModal] = useState(false)
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
    void loadPatientDetails(patient.id)
  }

  function openCreatePanel() {
    setShowCreateModal(true)
  }

  function closePanel() {
    clearPatientDetails()
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
      headerActions={
        <Button
          label="Add patient"
          variant="primary"
          icon={<Plus className="size-4" />}
          onClick={openCreatePanel}
        />
      }
      pagination={{
        type: 'cursor',
        page,
        hasNextPage: hasMore,
        canGoBack,
        onNextPage: goToNextPage,
        onPreviousPage: goToPreviousPage,
      }}
      filters={
        <TableFilters fields={patientFiltersWithStatus} values={filters} onChange={setFilters} />
      }
      detailsPanel={
        patientId !== null ? (
          <PatientDetailsPanel
            key={patientId}
            patientId={patientId}
            diagnoses={diagnoses}
            bookings={bookings}
            loading={detailsLoading}
            error={detailsError}
            onRetry={reloadPatientDetails}
            onClose={closePanel}
            onActionCompleted={reloadPatientDetails}
          />
        ) : undefined
      }
    >
      {showCreateModal && (
        <PatientCreateModal
          onClose={() => setShowCreateModal(false)}
          onCreated={handlePatientCreated}
        />
      )}
    </MainPage>
  )
}
