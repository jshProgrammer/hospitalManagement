type TableProps<T> = {
  columns: (keyof T)[]
  data: T[]
  rowStart: number
}

function formatHeader(key: PropertyKey) {
  return String(key)
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, char => char.toUpperCase())
}

export default function Table<T>({ columns, data, rowStart }: TableProps<T>) {
  return (
    <div className="h-full overflow-auto">
      <table className="w-full min-w-max text-center text-sm">
        <thead className="bg-accent text-light sticky top-0 z-10">
          <tr className="divide-x divide-white/15">
            <th> </th> {/* Empty column for row number */}
            {columns.map(column => (
              <th key={String(column)} className="px-4 py-3 font-semibold">
                {formatHeader(column)}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-border divide-y">
          {data.map((row, rowIndex) => (
            <tr
              key={rowIndex}
              className="divide-border hover:bg-highlight bg-elevated even:bg-background divide-x transition-colors"
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
          ))}
        </tbody>
      </table>
    </div>
  )
}