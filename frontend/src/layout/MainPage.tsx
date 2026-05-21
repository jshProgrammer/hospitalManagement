import PageHeader from './PageHeader.tsx'
import Table from '../components/Table.tsx'
import LoadingIcon from '../components/LoadingIcon.tsx'
import ErrorComponent from '../components/ErrorComponent.tsx'
import type { ReactNode } from 'react'
import Pagination from '../components/Pagination.tsx'
import { DEFAULT_PAGE_SIZE } from '../hooks/usePageData.tsx'

type MainPageProps<T> = {
  title: string
  columns: (keyof T)[]
  data: T[]
  loading: boolean
  error: string | null
  onRetry: () => void
  filters?: ReactNode
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}
export default function MainPage<T>({
  title,
  columns,
  data,
  loading,
  error,
  onRetry,
  filters,
  page,
  totalPages,
  onPageChange,
}: MainPageProps<T>) {
  const hasData = data.length > 0

  return (
    <div className="flex h-full flex-col">
      <PageHeader title={title} />
      {filters}
      <div className="min-h-0 flex-1">
        <div className="border-border bg-surface flex h-full w-full flex-col overflow-hidden rounded-lg border shadow-sm">
          {error && !hasData && <ErrorComponent message={error} onRetry={onRetry} />}
          {!hasData && loading && <LoadingIcon />}
          {hasData && (
            <>
              <div className="min-h-0 flex-1 overflow-hidden">
                <Table columns={columns} data={data} rowStart={page * DEFAULT_PAGE_SIZE} />
              </div>
              {loading && (
                <div className="border-border border-t py-3">
                  <LoadingIcon />
                </div>
              )}
              {totalPages > 1 && !loading && (
                <div>
                  <Pagination page={page} totalPages={totalPages} onPageChange={onPageChange} />
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}