import { useMemo, useState } from 'react'

export type FilterValues = Record<string, string>

const defaultClientOnlyKeys: string[] = []

export function useTableFilters(baseUrl: string, clientOnlyKeys = defaultClientOnlyKeys) {
  const [filters, setFilters] = useState<FilterValues>({})

  const url = useMemo(() => {
    const [path, query = ''] = baseUrl.split('?')
    const params = new URLSearchParams(query)
    const clientOnlyKeySet = new Set(clientOnlyKeys)

    Object.entries(filters).forEach(([key, value]) => {
      if (value && !clientOnlyKeySet.has(key)) {
        params.set(key, value)
      }
    })

    return `${path}?${params.toString()}`
  }, [baseUrl, clientOnlyKeys, filters])

  return { filters, setFilters, url }
}
