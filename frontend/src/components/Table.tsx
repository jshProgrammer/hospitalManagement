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
    <div className="border-border overflow-auto rounded-xl border">
      <table className="w-full text-center text-sm">
        <thead className="bg-accent text-light sticky top-0 z-10">
          <tr className="divide-border divide-x">
            {columns.map(column => (
              <th key={String(column.key)} className="px-3 py-3 font-semibold">
                {column.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-border divide-y">
          {data.map((row, rowIndex) => (
            <tr key={rowIndex} className="divide-border even:bg-background odd:bg-surface divide-x">
              {columns.map(column => (
                <td key={String(column.key)} className="text-dark px-3 py-3 whitespace-nowrap">
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