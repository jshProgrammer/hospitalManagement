import { useEffect, useMemo, useState } from 'react'

export type FilterValues = Record<string, string>

export function useTableFilters(baseUrl: string) {
  const [filters, setFilters] = useState<FilterValues>(() => readStoredFilters(baseUrl))

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

  useEffect(() => {
    const key = getStorageKey(baseUrl)
    const activeFilters = Object.fromEntries(
      Object.entries(filters).filter(([, value]) => Boolean(value))
    )

    if (Object.keys(activeFilters).length === 0) {
      window.localStorage.removeItem(key)
      return
    }

    window.localStorage.setItem(key, JSON.stringify(activeFilters))
  }, [baseUrl, filters])

  return { filters, setFilters, url }
}

function readStoredFilters(baseUrl: string): FilterValues {
  try {
    const stored = window.localStorage.getItem(getStorageKey(baseUrl))

    if (!stored) {
      return {}
    }

    const parsed = JSON.parse(stored) as unknown

    if (!isFilterValues(parsed)) {
      return {}
    }

    return parsed
  } catch {
    return {}
  }
}

function getStorageKey(baseUrl: string) {
  return `hospital-management:filters:${baseUrl}`
}

function isFilterValues(value: unknown): value is FilterValues {
  if (typeof value !== 'object' || value === null || Array.isArray(value)) {
    return false
  }

  return Object.values(value).every(item => typeof item === 'string')
}
