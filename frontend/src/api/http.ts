export type ApiFieldErrors = Record<string, string>

export class ApiRequestError extends Error {
  status: number
  fields?: ApiFieldErrors

  constructor(message: string, status: number, fields?: ApiFieldErrors) {
    super(message)
    this.name = 'ApiRequestError'
    this.status = status
    this.fields = fields
  }
}

type RequestOptions = Omit<RequestInit, 'body'> & {
  body?: unknown
}

export async function requestJson<T>(url: string, options: RequestOptions = {}): Promise<T> {
  const response = await fetch(url, {
    ...options,
    headers: {
      ...(options.body ? { 'Content-Type': 'application/json' } : {}),
      ...options.headers,
    },
    body: options.body ? JSON.stringify(options.body) : undefined,
  })

  const payload = await readJson(response)

  if (!response.ok) {
    throw new ApiRequestError(
      getErrorMessage(response, payload),
      response.status,
      getFieldErrors(payload)
    )
  }

  return payload as T
}

async function readJson(response: Response): Promise<unknown> {
  const contentType = response.headers.get('content-type')

  if (!contentType?.includes('application/json')) {
    return null
  }

  return response.json()
}

function getErrorMessage(response: Response, payload: unknown) {
  if (isObject(payload) && payload.fields && isObject(payload.fields)) {
    return 'Please check the highlighted fields.'
  }

  if (response.status === 409) {
    return 'The request conflicts with existing data.'
  }

  if (response.status === 404) {
    return 'The requested record was not found.'
  }

  if (response.status === 429) {
    return 'Too many requests. Please wait before trying again.'
  }

  if (response.status >= 400 && response.status < 500) {
    return 'Please check the submitted data.'
  }

  return 'The server could not complete the request.'
}

function getFieldErrors(payload: unknown): ApiFieldErrors | undefined {
  if (!isObject(payload) || !isObject(payload.fields)) {
    return undefined
  }

  return Object.fromEntries(
    Object.entries(payload.fields).filter((entry): entry is [string, string] => {
      const [, value] = entry
      return typeof value === 'string'
    })
  )
}

function isObject(value: unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null
}
