import { useEffect, useState } from 'react'

type PageResponse<TApi> = {
  content: TApi[]
}
export function usePageData<TApi, TData>(url: string, mapper: (item: TApi) => TData) {
  const [data, setData] = useState<TData[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    async function fetchData() {
      try {
        setLoading(true)
        setError(null)
        const response = await fetch(url)

        if (!response.ok) {
          setError(`Error fetching data: ${response.statusText} (${response.status})`)
          return
        }
        const page: PageResponse<TApi> = await response.json()
        setData(page.content.map(mapper))
      } catch (err) {
        console.error(err)
        setError(err instanceof Error ? err.message : 'Unknown Error')
      } finally {
        setLoading(false)
      }
    }
    void fetchData()
  }, [url, mapper])

  return { data, loading, error }
}