import { useEffect, useState } from 'react'
import { requestJson } from '../api/http'
import type { RoomApi } from '../types/Bookings'

type PageResponse<T> = {
  content: T[]
}

type RoomOptionsState = {
  rooms: RoomApi[]
  loading: boolean
  error: string | null
}

const initialState: RoomOptionsState = {
  rooms: [],
  loading: false,
  error: null,
}

export function useRoomOptions(query: string) {
  const [state, setState] = useState<RoomOptionsState>(initialState)
  const debouncedQuery = useDebouncedValue(query.trim(), 250)

  useEffect(() => {
    const controller = new AbortController()

    async function loadRooms() {
      try {
        setState(current => ({ ...current, loading: true, error: null }))

        const params = new URLSearchParams({
          page: '0',
          size: '50',
          sort: 'number,asc',
        })

        const floorMatch = debouncedQuery.match(/^floor\s+(\d+)$/i)

        if (floorMatch) {
          params.set('floor', floorMatch[1])
        } else if (/^\d+$/.test(debouncedQuery)) {
          params.set('number', debouncedQuery)
        }

        const response = await requestJson<PageResponse<RoomApi>>(`/api/rooms?${params}`, {
          signal: controller.signal,
        })

        if (controller.signal.aborted) {
          return
        }

        setState({
          rooms: response.content,
          loading: false,
          error: null,
        })
      } catch (error) {
        if (controller.signal.aborted) {
          return
        }

        setState({
          rooms: [],
          loading: false,
          error: error instanceof Error ? error.message : 'Could not load rooms.',
        })
      }
    }

    void loadRooms()

    return () => controller.abort()
  }, [debouncedQuery])

  return state
}

function useDebouncedValue(value: string, delayMs: number) {
  const [debouncedValue, setDebouncedValue] = useState(value)

  useEffect(() => {
    const timeout = window.setTimeout(() => setDebouncedValue(value), delayMs)
    return () => window.clearTimeout(timeout)
  }, [delayMs, value])

  return debouncedValue
}
