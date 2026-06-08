import { useState, type Key } from 'react'

type TableProps<T> = {
  columns: (keyof T)[]
  data: T[]
  rowStart: number
  onRowClick?: (row: T) => void
  getRowKey?: (row: T) => Key
}

function formatHeader(key: PropertyKey) {
  return String(key)
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, char => char.toUpperCase())
}

export default function Table<T>({
  columns,
  data,
  rowStart,
  onRowClick,
  getRowKey,
}: TableProps<T>) {
  const [selectedRowKey, setSelectedRowKey] = useState<Key | null>(null)

  function handleRowClick(row: T, rowKey: Key) {
    if (!onRowClick) {
      return
    }

    setSelectedRowKey(rowKey)
    onRowClick(row)
  }

  return (
    <div className="h-full overflow-auto">
      <table className="w-full min-w-max text-center text-sm">
        <thead className="bg-accent text-light sticky top-0 z-10">
          <tr className="divide-x divide-white/15">
            <th className="px-4 py-3">#</th>

            {columns.map(column => (
              <th key={String(column)} className="px-4 py-3 font-semibold">
                {formatHeader(column)}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-border divide-y">
          {data.map((row, rowIndex) => {
            const rowKey = getRowKey?.(row) ?? rowIndex
            const isSelected = selectedRowKey === rowKey

            return (
              <tr
                key={rowKey}
                onClick={onRowClick ? () => handleRowClick(row, rowKey) : undefined}
                className={[
                  'divide-border divide-x transition-colors',
                  'bg-elevated even:bg-background',
                  onRowClick ? 'hover:bg-highlight cursor-pointer' : '',
                  isSelected
                    ? 'bg-accent/20! even:bg-accent/20! outline-accent outline-2 outline-offset-[-2px]'
                    : '',
                ].join(' ')}
              >
                <td className="text-muted px-4 py-3 font-medium whitespace-nowrap">
                  {rowStart + rowIndex + 1}
                </td>
                {columns.map(column => (
                  <td key={String(column)} className="text-dark px-4 py-3 whitespace-nowrap">
                    {String(row[column] ?? '')}
                  </td>
                ))}
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}