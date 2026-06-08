import MainPage from '../layout/MainPage.tsx'
import type { Nurse, NurseApi } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'
import { useCursorPageData } from '../hooks/useCursorPageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { nurseFilters } from '../constants/filters.ts'
import { personColumns } from '../constants/columns.ts'

const columns = [
  ...personColumns,
  'department',
  'stationId',
  'stationName',
  'stationDepartmentId',
  'stationDepartmentName',
  'building',
] satisfies (keyof Nurse)[]

export function Nurses() {
  const { filters, setFilters, url } = useTableFilters(`/api/nurses`)
  const { data, loading, error, reload, page, hasMore, canGoBack, goToNextPage, goToPreviousPage } =
    useCursorPageData<NurseApi, Nurse, 'nurses'>(url, 'nurses', mapNurse)

  return (
    <MainPage
      title="Nurses"
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
      filters={<TableFilters fields={nurseFilters} values={filters} onChange={setFilters} />}
    />
  )
}
