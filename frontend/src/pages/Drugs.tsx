import { useEffect, useState } from 'react'
import MainPage from '../layout/MainPage.tsx'
import Table from '../components/Table.tsx'
import type { Drug, DrugPage } from '../types/Drug.tsx'
import { mapDrug } from '../mapper/drugMapper.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'activeIngredient', header: 'Wirkstoff' },
  { key: 'type', header: 'Typ' },
  { key: 'stock', header: 'Bestand' },
] satisfies { key: keyof Drug; header: string }[]

export function Drugs() {
  const [drugs, setDrugs] = useState<Drug[]>([])

  useEffect(() => {
    async function fetchDrugs() {
      const response = await fetch('api/drugs?sort=id,asc&size=30')
      const data: DrugPage = await response.json()

      setDrugs(data.content.map(mapDrug))
    }

    fetchDrugs().catch(console.error)
  }, [])

  return (
    <MainPage title="Drugs">
      <Table columns={columns} data={drugs} />
    </MainPage>
  )
}