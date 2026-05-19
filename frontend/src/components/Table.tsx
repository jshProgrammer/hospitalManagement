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
      <table className="w-full min-w-max text-center text-sm">
        <thead className="bg-accent text-light sticky top-0 z-10">
          <tr className="divide-x divide-white/15">
            {columns.map(column => (
              <th key={String(column.key)} className="px-4 py-3 font-semibold">
                {column.header}
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
