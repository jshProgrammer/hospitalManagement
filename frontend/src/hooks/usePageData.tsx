import { useCallback, useEffect, useRef, useState } from 'react'

const BATCH_SIZE = 5
export const DEFAULT_PAGE_SIZE = 50

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

  const buildUrl = useCallback(
    (batchPage: number) => {
      const [path, query = ''] = endpoint.split('?')
      const params = new URLSearchParams(query)

      params.set('sort', 'id,asc')
      params.set('page', String(batchPage))
      params.set('size', String(BATCH_SIZE))

      return `${path}?${params.toString()}`
    },
    [endpoint]
  )

  const loadData = useCallback(async () => {
    controllerRef.current?.abort()

    const controller = new AbortController()
    controllerRef.current = controller

    const firstBatchPage = page * (DEFAULT_PAGE_SIZE / BATCH_SIZE)
    const batchCount = DEFAULT_PAGE_SIZE / BATCH_SIZE

    try {
      setLoading(true)
      setError(null)
      setData([])
      setNumberOfElements(0)

      for (let index = 0; index < batchCount; index++) {
        if (controller.signal.aborted) {
          return
        }

        const response = await fetch(buildUrl(firstBatchPage + index), {
          signal: controller.signal,
        })

        if (!response.ok) {
          setError(`Error fetching data: ${response.statusText} (${response.status})`)
          return
        }

        const pageResponse: PageResponse<TApi> = await response.json()
        const mappedData = pageResponse.content.map(mapper)

        setData(previous => [...previous, ...mappedData])
        setTotalElements(pageResponse.totalElements)
        setTotalPages(Math.ceil(pageResponse.totalElements / DEFAULT_PAGE_SIZE))
        setNumberOfElements(previous => previous + pageResponse.numberOfElements)

        if (pageResponse.content.length < BATCH_SIZE) {
          break
        }
      }
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
  }, [buildUrl, mapper, page])

  useEffect(() => {
    void loadData()

    return () => {
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