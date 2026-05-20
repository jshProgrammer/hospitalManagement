import MainPage from '../layout/MainPage.tsx'
import type { Station, StationApi } from '../types/Station.tsx'
import { mapStation } from '../mapper/stationMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { stationFilters } from '../constants/filters.tsx'
import { useState } from 'react'

const columns = [
  { key: 'name', header: 'Name' },
  { key: 'departmentId', header: 'Abteilungs-ID' },
  { key: 'departmentName', header: 'Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Station; header: string }[]

export function Stations() {
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/stations`)
  const { data, loading, error, reload, totalPages } = usePageData<StationApi, Station>(
    url,
    page,
    mapStation
  )

  return (
    <MainPage
      title="Stations"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={stationFilters} values={filters} onChange={setFilters} />}
    />
  )
}