import { useCallback, useEffect, useRef, useState } from 'react'

type PageResponse<TApi> = {
  content: TApi[]
}
export function usePageData<TApi, TData>(url: string, mapper: (item: TApi) => TData) {
  const [data, setData] = useState<TData[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const controllerRef = useRef<AbortController | null>(null)

  const loadData = useCallback(
    async () => {
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
    },
    [url, mapper]
  )

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
  return { data, loading, error, reload: loadData }
}
