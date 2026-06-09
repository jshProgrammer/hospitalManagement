import PageHeader from './PageHeader.tsx'
import Table from '../components/Table.tsx'
import LoadingIcon from '../components/LoadingIcon.tsx'
import ErrorComponent from '../components/ErrorComponent.tsx'
import type { Key, ReactNode } from 'react'
import Pagination from '../components/Pagination.tsx'
import { DEFAULT_PAGE_SIZE } from '../hooks/usePageData.tsx'
import CursorPagination from '../components/CursorPagination.tsx'

type PagePagination = {
  type: 'page'
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

type CursorPagePagination = {
  type: 'cursor'
  page: number
  hasNextPage: boolean
  canGoBack: boolean
  onNextPage: () => void
  onPreviousPage: () => void
}

type MainPageProps<T> = {
  title: string
  columns: (keyof T)[]
  data: T[]
  loading: boolean
  error: string | null
  onRetry: () => void
  filters?: ReactNode
  pagination: PagePagination | CursorPagePagination
  onRowClick?: (row: T) => void
  getRowKey?: (row: T) => Key
  detailsPanel?: ReactNode
}
export default function MainPage<T>({
  title,
  columns,
  data,
  loading,
  error,
  onRetry,
  filters,
  pagination,
  onRowClick,
  getRowKey,
  detailsPanel,
}: MainPageProps<T>) {
  const hasData = data.length > 0
  const page = pagination.page

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
                <div className="flex h-full min-w-0">
                  <div className="min-w-0 flex-1 overflow-hidden">
                    <Table
                      columns={columns}
                      data={data}
                      rowStart={page * DEFAULT_PAGE_SIZE}
                      onRowClick={onRowClick}
                      getRowKey={getRowKey}
                    />
                  </div>

                  {detailsPanel && (
                    <aside className="border-border bg-elevated w-105 shrink-0 border-l">
                      {detailsPanel}
                    </aside>
                  )}
                </div>
              </div>
              {loading && (
                <div className="border-border border-t py-3">
                  <LoadingIcon />
                </div>
              )}
              {error && !loading && <ErrorComponent message={error} onRetry={onRetry} />}
              {pagination.type === 'page' && pagination.totalPages > 1 && !loading && (
                <Pagination
                  page={pagination.page}
                  totalPages={pagination.totalPages}
                  onPageChange={pagination.onPageChange}
                />
              )}
              {pagination.type === 'cursor' &&
                (pagination.canGoBack || pagination.hasNextPage) &&
                !loading && (
                  <CursorPagination
                    page={pagination.page}
                    hasNextPage={pagination.hasNextPage}
                    canGoBack={pagination.canGoBack}
                    onNextPage={pagination.onNextPage}
                    onPreviousPage={pagination.onPreviousPage}
                  />
                )}
            </>
          )}
        </div>
      </div>
    </div>
  )
}