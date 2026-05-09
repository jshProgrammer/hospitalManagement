import type { Station, StationApi } from '../types/Station.tsx'

export function mapStation(station: StationApi): Station {
  return {
    id: station.id,
    name: station.name,
    departmentId: station.department.id,
    departmentName: station.department.name,
    building: station.department.building,
  }
}