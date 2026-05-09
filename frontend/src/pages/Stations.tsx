import { useEffect, useState } from 'react'
import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'
import type { Station, StationPage } from '../types/Station.tsx'
import { mapStation } from '../mapper/stationMapper.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'departmentId', header: 'Abteilungs-ID' },
  { key: 'departmentName', header: 'Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Station; header: string }[]

export function Stations() {
  const [stations, setStations] = useState<Station[]>([])

  useEffect(() => {
    async function fetchStations() {
      const response = await fetch('api/stations?sort=id,asc&size=30')
      const data: StationPage = await response.json()

      setStations(data.content.map(mapStation))
    }

    fetchStations().catch(console.error)
  }, [])

  return (
    <MainPage title="Stations">
      <Table columns={columns} data={stations} />
    </MainPage>
  )
}