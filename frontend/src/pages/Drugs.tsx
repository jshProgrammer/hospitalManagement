import MainPage from '../layout/MainPage.tsx'
import type { Drug, DrugApi } from '../types/Drug.tsx'
import { mapDrug } from '../mapper/drugMapper.tsx'
import { usePageData } from '../hooks/usePageData.tsx'
import { useTableFilters } from '../hooks/useTableFilters.tsx'
import TableFilters from '../components/TableFilters.tsx'
import { drugFilters } from '../constants/filters.tsx'
import { useState } from 'react'

const columns = [
  { key: 'name', header: 'Name' },
  { key: 'activeIngredient', header: 'Wirkstoff' },
  { key: 'type', header: 'Typ' },
  { key: 'stock', header: 'Bestand' },
] satisfies { key: keyof Drug; header: string }[]

export function Drugs() {
  const [page, setPage] = useState(0)
  const { filters, setFilters, url } = useTableFilters(`/api/drugs`)
  const { data, loading, error, reload, totalPages } = usePageData<DrugApi, Drug>(
    url,
    page,
    mapDrug
  )

  return (
    <MainPage
      title="Drugs"
      columns={columns}
      data={data}
      loading={loading}
      error={error}
      onRetry={reload}
      page={page}
      totalPages={totalPages}
      onPageChange={setPage}
      filters={<TableFilters fields={drugFilters} values={filters} onChange={setFilters} />}
    />
  )
}