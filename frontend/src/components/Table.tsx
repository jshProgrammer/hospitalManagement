type TableColumn<T> = {
  key: keyof T
  header: string
}

type TableProps<T> = {
  columns: TableColumn<T>[]
  data: T[]
}

export default function Table<T>({ columns, data }: TableProps<T>) {
  return (
    <div className="h-full overflow-auto">
      <table className="w-full min-w-max text-left text-sm">
        <thead className="bg-surface-muted text-muted border-border sticky top-0 z-10 border-b">
          <tr>
            {columns.map(column => (
              <th
                key={String(column.key)}
                className="px-4 py-3 text-xs font-semibold tracking-wide whitespace-nowrap uppercase"
              >
                {column.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-border divide-y">
          {data.map((row, rowIndex) => (
            <tr key={rowIndex} className="hover:bg-primary-muted/60 transition-colors">
              {columns.map(column => (
                <td key={String(column.key)} className="text-dark px-4 py-3 whitespace-nowrap">
                  {String(row[column.key] ?? '')}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
