import MainPage from '../layout/MainPage.tsx'
import type { Patient, PatientApi } from '../types/Patient.tsx'
import { mapPatient } from '../mapper/patientMapper.tsx'
import { useCursorPageData } from '../hooks/useCursorPageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { patientFilters } from '../constants/filters.ts'
import { personColumns } from '../constants/columns.ts'
import { usePatientDetails } from '../hooks/usePatientDetails.tsx'

const columns = [...personColumns] satisfies (keyof Patient)[]

export function Patients() {
  const { filters, setFilters, url } = useTableFilters(`/api/patients`)
  const { data, loading, error, reload, page, hasMore, canGoBack, goToNextPage, goToPreviousPage } =
    useCursorPageData<PatientApi, Patient, 'patients'>(url, 'patients', mapPatient)

  const { loadPatientDetails } = usePatientDetails()
  async function handlePatientClick(patient: Patient) {
    const details = await loadPatientDetails(patient.id)
    if (!details) {
      return
    }

    console.log('Patient:', patient)
    console.log('Diagnosen:', details.diagnoses)
    console.log('Buchungen:', details.bookings)
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
      pagination={{
        type: 'cursor',
        page,
        hasNextPage: hasMore,
        canGoBack,
        onNextPage: goToNextPage,
        onPreviousPage: goToPreviousPage,
      }}
      filters={<TableFilters fields={patientFilters} values={filters} onChange={setFilters} />}
    />
  )
}