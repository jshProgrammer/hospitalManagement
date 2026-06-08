import MainPage from '../layout/MainPage.tsx'
import type { Station, StationApi } from '../types/Station.tsx'
import { mapStation } from '../mapper/stationMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { stationFilters } from '../constants/filters.ts'
import { useState } from 'react'

const columns = ['name', 'departmentId', 'departmentName', 'building'] satisfies (keyof Station)[]

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
      pagination={{
        type: 'page',
        page,
        totalPages,
        onPageChange: setPage,
      }}
      filters={<TableFilters fields={stationFilters} values={filters} onChange={setFilters} />}
    />
  )
}
