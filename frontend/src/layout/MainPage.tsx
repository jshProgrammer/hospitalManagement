import PageHeader from './PageHeader.tsx'
import Table from '../components/Table.tsx'
import LoadingIcon from '../components/LoadingIcon.tsx'
import ErrorComponent from '../components/ErrorComponent.tsx'
import type { ReactNode } from 'react'

type Column<T> = {
  key: keyof T
  header: string
}
type MainPageProps<T> = {
  title: string
  columns: Column<T>[]
  data: T[]
  loading: boolean
  error: string | null
  onRetry: () => void
  filters?: ReactNode
  rowStart: number
}
export default function MainPage<T>({
  title,
  columns,
  data,
  loading,
  error,
  onRetry,
  filters,
  rowStart,
}: MainPageProps<T>) {
  return (
    <div className="flex h-full flex-col">
      <PageHeader title={title} />
      {filters}
      <div className="min-h-0 flex-1">
        <div className="border-border bg-surface h-full w-full overflow-hidden rounded-lg border shadow-sm">
          {error && <ErrorComponent message={error} onRetry={onRetry} />}
          {loading && <LoadingIcon />}
          {!loading && !error && <Table columns={columns} data={data} rowStart={rowStart} />}
        </div>
      </div>
    </div>
  )
}