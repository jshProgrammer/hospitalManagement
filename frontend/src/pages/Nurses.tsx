import { useEffect, useState } from 'react'
import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'
import type { Nurse, NursePage } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'employeeId', header: 'Employee ID' },
  { key: 'personId', header: 'Person ID' },
  { key: 'gender', header: 'Geschlecht' },
  { key: 'firstName', header: 'Vorname' },
  { key: 'lastName', header: 'Nachname' },
  { key: 'plz', header: 'PLZ' },
  { key: 'city', header: 'Stadt' },
  { key: 'street', header: 'Straße' },
  { key: 'streetNo', header: 'Hausnummer' },
  { key: 'country', header: 'Land' },
  { key: 'birthday', header: 'Geburtsdatum' },
  { key: 'phone', header: 'Telefon' },
  { key: 'email', header: 'E-Mail' },
  { key: 'department', header: 'Abteilung' },
  { key: 'stationId', header: 'Stations-ID' },
  { key: 'stationName', header: 'Station' },
  { key: 'stationDepartmentId', header: 'Stations-Abteilungs-ID' },
  { key: 'stationDepartmentName', header: 'Stations-Abteilung' },
  { key: 'building', header: 'Gebäude' },
] satisfies { key: keyof Nurse; header: string }[]

export function Nurses() {
  const [nurses, setNurses] = useState<Nurse[]>([])

  useEffect(() => {
    async function fetchNurses() {
      const response = await fetch('api/nurses?size=20')
      const data: NursePage = await response.json()

      setNurses(data.content.map(mapNurse))
    }

    fetchNurses().catch(console.error)
  }, [])

  return (
    <MainPage title="Nurses">
      <Table columns={columns} data={nurses} />
    </MainPage>
  )
}