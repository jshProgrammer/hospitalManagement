import MainPage from '../layout/MainPage.tsx'
import type { Nurse, NurseApi } from '../types/Nurse.tsx'
import { mapNurse } from '../mapper/nurseMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'

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
  const { data, loading, error } = usePageData<NurseApi, Nurse>(
    'api/nurses?sort=id,asc&size=30',
    mapNurse
  )

  return <MainPage title="Nurses" columns={columns} data={data} loading={loading} error={error} />
}