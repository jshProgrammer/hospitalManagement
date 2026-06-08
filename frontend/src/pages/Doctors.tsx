import MainPage from '../layout/MainPage.tsx'
import type { Doctor, DoctorApi } from '../types/Doctor.tsx'
import { mapDoctor } from '../mapper/doctorMapper.tsx'
import { useCursorPageData } from '../hooks/useCursorPageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { doctorFilters } from '../constants/filters.ts'
import { personColumns } from '../constants/columns.ts'

const columns = [...personColumns, 'department', 'workPhone', 'type'] satisfies (keyof Doctor)[]

export function Doctors() {
  const { filters, setFilters, url } = useTableFilters(`/api/doctors`)
  const { data, loading, error, reload, page, hasMore, canGoBack, goToNextPage, goToPreviousPage } =
    useCursorPageData<DoctorApi, Doctor, 'doctors'>(url, 'doctors', mapDoctor)

  return (
    <MainPage
      title="Doctors"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      pagination={{
        type: 'cursor',
        page,
        hasNextPage: hasMore,
        canGoBack,
        onNextPage: goToNextPage,
        onPreviousPage: goToPreviousPage,
      }}
      filters={<TableFilters fields={doctorFilters} values={filters} onChange={setFilters} />}
    />
  )
}
