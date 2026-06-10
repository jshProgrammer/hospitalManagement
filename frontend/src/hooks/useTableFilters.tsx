import { useMemo, useState } from 'react'

export type FilterValues = Record<string, string>

export function useTableFilters(baseUrl: string) {
  const [filters, setFilters] = useState<FilterValues>({})

  const url = useMemo(() => {
    const [path, query = ''] = baseUrl.split('?')
    const params = new URLSearchParams(query)

    Object.entries(filters).forEach(([key, value]) => {
      if (!value) {
        return
      }

      const values = value.split(',').filter(Boolean)

      if (values.length > 1) {
        values.forEach(item => params.append(key, item))
      } else {
        params.set(key, values[0] ?? value)
      }
    })

    return `${path}?${params.toString()}`
  }, [baseUrl, filters])

  return { filters, setFilters, url }
}
