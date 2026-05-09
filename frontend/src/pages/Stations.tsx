import MainPage from '../layout/MainPage.tsx'
import type { Station, StationApi } from '../types/Station.tsx'
import { mapStation } from '../mapper/stationMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'departmentId', header: 'Abteilungs-ID' },
  { key: 'departmentName', header: 'Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Station; header: string }[]

export function Stations() {
  const { data, loading, error } = usePageData<StationApi, Station>(
    'api/stations?sort=id,asc&size=30',
    mapStation
  )

  return <MainPage title="Stations" columns={columns} data={data} loading={loading} error={error} />
}