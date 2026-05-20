import { useCallback, useEffect, useMemo, useRef, useState } from 'react'
import { DEFAULT_PAGE_SIZE } from '../constants/pagination.tsx'

type PageResponse<TApi> = {
  content: TApi[]
  totalPages: number
  totalElements: number
  numberOfElements: number
  size: number
}
export function usePageData<TApi, TData>(
  endpoint: string,
  page: number,
  mapper: (item: TApi) => TData
) {
  const [data, setData] = useState<TData[]>([])
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [numberOfElements, setNumberOfElements] = useState(0)

  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const controllerRef = useRef<AbortController | null>(null)

  const url = useMemo(() => {
    return `${endpoint}?sort=id,asc&page=${page}&size=${DEFAULT_PAGE_SIZE}`
  }, [endpoint, page, DEFAULT_PAGE_SIZE])

  const loadData = useCallback(async () => {
    controllerRef.current?.abort()

    const controller = new AbortController()
    controllerRef.current = controller

    try {
      await Promise.resolve()
      if (controller.signal.aborted) {
        return
      }

      setLoading(true)
      setError(null)
      const response = await fetch(url, { signal: controller.signal })

      if (!response.ok) {
        setError(`Error fetching data: ${response.statusText} (${response.status})`)
        return
      }
      const page: PageResponse<TApi> = await response.json()

      setData(page.content.map(mapper))
      setTotalPages(page.totalPages)
      setTotalElements(page.totalElements)
      setNumberOfElements(page.numberOfElements)
    } catch (err) {
      if (err instanceof DOMException && err.name === 'AbortError') {
        return
      }

      console.error(err)
      setError(err instanceof Error ? err.message : 'Unknown Error')
    } finally {
      if (controllerRef.current === controller) {
        setLoading(false)
      }
    }
  }, [url, mapper])

  useEffect(() => {
    let cancelled = false

    queueMicrotask(() => {
      if (!cancelled) {
        void loadData()
      }
    })

    return () => {
      cancelled = true
      controllerRef.current?.abort()
    }
  }, [loadData])
  return {
    data,
    loading,
    error,
    reload: loadData,
    totalPages,
    totalElements,
    numberOfElements,
  }
}