import { useMemo, useState } from 'react'

export type FilterValues = Record<string, string>

export function useTableFilters(baseUrl: string) {
  const [filters, setFilters] = useState<FilterValues>({})

  const url = useMemo(() => {
    const [path, query = ''] = baseUrl.split('?')
    const params = new URLSearchParams(query)

    Object.entries(filters).forEach(([key, value]) => {
      if (value) {
        params.set(key, value)
      }
    })

    return `${path}?${params.toString()}`
  }, [baseUrl, filters])

  return { filters, setFilters, url }
}
