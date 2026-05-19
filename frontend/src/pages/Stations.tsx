import MainPage from '../layout/MainPage.tsx'
import type { Station, StationApi } from '../types/Station.tsx'
import { mapStation } from '../mapper/stationMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { stationFilters } from '../constants/filters.tsx'
import { DEFAULT_PAGE_SIZE } from '../constants/pagination.tsx'

const columns = [
  { key: 'name', header: 'Name' },
  { key: 'departmentId', header: 'Abteilungs-ID' },
  { key: 'departmentName', header: 'Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Station; header: string }[]

export function Stations() {
  const { filters, setFilters, url } = useTableFilters(
    `/api/stations?sort=id,asc&size=${DEFAULT_PAGE_SIZE}`
  )
  const { data, loading, error, reload } = usePageData<StationApi, Station>(url, mapStation)

  return (
    <MainPage
      title="Stations"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      rowStart={0 * DEFAULT_PAGE_SIZE}
      filters={<TableFilters fields={stationFilters} values={filters} onChange={setFilters} />}
    />
  )
}