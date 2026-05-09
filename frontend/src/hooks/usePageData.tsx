import { useCallback, useEffect, useState } from 'react'

type PageResponse<TApi> = {
  content: TApi[]
}
export function usePageData<TApi, TData>(url: string, mapper: (item: TApi) => TData) {
  const [data, setData] = useState<TData[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const loadData = useCallback(
    async (signal?: AbortSignal) => {
      try {
        setLoading(true)
        setError(null)
        const response = await fetch(url, { signal })

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
        setLoading(false)
      }
    },
    [url, mapper]
  )

  useEffect(() => {
    const controller = new AbortController()

    void loadData(controller.signal)

    return () => {
      controller.abort()
    }
  }, [loadData])
  return { data, loading, error, reload: loadData }
}