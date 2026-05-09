import MainPage from '../layout/MainPage.tsx'
import type { Drug, DrugApi } from '../types/Drug.tsx'
import { mapDrug } from '../mapper/drugMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'activeIngredient', header: 'Wirkstoff' },
  { key: 'type', header: 'Typ' },
  { key: 'stock', header: 'Bestand' },
] satisfies { key: keyof Drug; header: string }[]

export function Drugs() {
  const { data, loading, error } = usePageData<DrugApi, Drug>(
    'api/drugs?sort=id,asc&size=30',
    mapDrug
  )
  return <MainPage title="Drugs" columns={columns} data={data} loading={loading} error={error} />
}