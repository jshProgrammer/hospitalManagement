import MainPage from '../layout/MainPage.tsx'
import type { Drug, DrugApi } from '../types/Drug.tsx'
import { mapDrug } from '../mapper/drugMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { drugFilters } from '../components/tableFilterConfigs.tsx'

const columns = [
  { key: 'id', header: 'ID' },
  { key: 'name', header: 'Name' },
  { key: 'activeIngredient', header: 'Wirkstoff' },
  { key: 'type', header: 'Typ' },
  { key: 'stock', header: 'Bestand' },
] satisfies { key: keyof Drug; header: string }[]

export function Drugs() {
  const { filters, setFilters, url } = useTableFilters('/api/drugs?sort=id,asc&size=30')
  const { data, loading, error, reload } = usePageData<DrugApi, Drug>(url, mapDrug)

  return (
    <MainPage
      title="Drugs"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      filters={<TableFilters fields={drugFilters} values={filters} onChange={setFilters} />}
    />
  )
}
