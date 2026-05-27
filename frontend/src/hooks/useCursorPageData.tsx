import { useCallback, useEffect, useRef, useState } from 'react'
import { DEFAULT_PAGE_SIZE } from './usePageData.tsx'

type CursorResponse<TApi, TCollectionKey extends string> = {
  [key in TCollectionKey]: TApi[]
} & {
  nextCursor: number
  hasMore: boolean
}

type CursorPaginationState = {
  endpoint: string
  page: number
  cursors: (number | undefined)[]
}

export function useCursorPageData<TApi, TData, TCollectionKey extends string>(
  endpoint: string,
  collectionKey: TCollectionKey,
  mapper: (item: TApi) => TData
) {
  const [data, setData] = useState<TData[]>([])
  const [hasMore, setHasMore] = useState(false)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [pagination, setPagination] = useState<CursorPaginationState>({
    endpoint,
    page: 0,
    cursors: [undefined],
  })
  const controllerRef = useRef<AbortController | null>(null)

  const endpointChanged = pagination.endpoint !== endpoint
  const page = endpointChanged ? 0 : pagination.page
  const cursors = endpointChanged ? [undefined] : pagination.cursors
  const after = cursors[page]

  const buildUrl = useCallback(
    (cursor?: number) => {
      const [path, query = ''] = endpoint.split('?')
      const params = new URLSearchParams(query)

      params.set('limit', String(DEFAULT_PAGE_SIZE))

      if (cursor && cursor > 0) {
        params.set('after', String(cursor))
      } else {
        params.delete('after')
      }

      return `${path}?${params.toString()}`
    },
    [endpoint]
  )

  const loadData = useCallback(async () => {
    controllerRef.current?.abort()

    const controller = new AbortController()
    controllerRef.current = controller

    try {
      setLoading(true)
      setError(null)
      setData([])

      const response = await fetch(buildUrl(after), {
        signal: controller.signal,
      })

      if (!response.ok) {
        setError(`Error fetching data: ${response.statusText} (${response.status})`)
        return
      }

      const cursorResponse = (await response.json()) as CursorResponse<TApi, TCollectionKey>
      const mappedData = cursorResponse[collectionKey].map(mapper)

      setData(mappedData)
      setHasMore(cursorResponse.hasMore)
      setPagination(current => {
        const nextCursor = cursorResponse.nextCursor || undefined

        if (current.endpoint !== endpoint) {
          return {
            endpoint,
            page: 0,
            cursors: [undefined, nextCursor],
          }
        }

        if (current.page !== page) {
          return current
        }

        const nextCursors = current.cursors.slice(0, page + 2)
        nextCursors[page + 1] = nextCursor

        return {
          ...current,
          cursors: nextCursors,
        }
      })
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
  }, [after, buildUrl, collectionKey, endpoint, mapper, page])

  useEffect(() => {
    const timeoutId = window.setTimeout(() => {
      void loadData()
    }, 0)

    return () => {
      window.clearTimeout(timeoutId)
      controllerRef.current?.abort()
    }
  }, [loadData])

  const goToNextPage = useCallback(() => {
    setPagination(current => {
      if (!hasMore) {
        return current
      }

      return {
        ...current,
        endpoint,
        page: current.endpoint === endpoint ? current.page + 1 : 0,
      }
    })
  }, [endpoint, hasMore])

  const goToPreviousPage = useCallback(() => {
    setPagination(current => ({
      ...current,
      endpoint,
      page: current.endpoint === endpoint ? Math.max(current.page - 1, 0) : 0,
    }))
  }, [endpoint])

  return {
    data,
    loading,
    error,
    reload: loadData,
    page,
    hasMore: !endpointChanged && hasMore,
    canGoBack: !endpointChanged && page > 0,
    goToNextPage,
    goToPreviousPage,
  }
}
