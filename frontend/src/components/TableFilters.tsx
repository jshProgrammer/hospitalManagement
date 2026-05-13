import { type FormEvent, useState } from 'react'

type BaseFilterField = {
  name: string
  label: string
  placeholder?: string
}

type TextFilterField = BaseFilterField & {
  type?: 'text' | 'number' | 'date'
}

type SelectFilterField = BaseFilterField & {
  type: 'select'
  options: { label: string; value: string }[]
}

export type FilterField = TextFilterField | SelectFilterField

type FilterValues = Record<string, string>

type TableFiltersProps = {
  fields: FilterField[]
  values: FilterValues
  onChange: (values: FilterValues) => void
}

export default function TableFilters({ fields, values, onChange }: TableFiltersProps) {
  const [draft, setDraft] = useState<FilterValues>(values)
  const [expanded, setExpanded] = useState(false)
  const activeFilterCount = Object.values(values).filter(Boolean).length

  function updateDraft(name: string, value: string) {
    setDraft(current => ({ ...current, [name]: value }))
  }

  function applyFilters(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onChange(cleanValues(draft))
    setExpanded(false)
  }

  function resetFilters() {
    setDraft({})
    onChange({})
    setExpanded(false)
  }

  return (
    <form
      onSubmit={applyFilters}
      className="border-border bg-elevated relative z-20 mb-4 rounded-lg border px-4 py-3 shadow-sm"
    >
      <div className="flex flex-col gap-3">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <button
            type="button"
            onClick={() => setExpanded(current => !current)}
            className="group flex min-w-0 items-center gap-2 text-left"
          >
            <span className="bg-accent/10 text-accent inline-flex h-8 w-8 items-center justify-center rounded-md">
              <svg
                aria-hidden="true"
                viewBox="0 0 24 24"
                className="h-4 w-4"
                fill="none"
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
              >
                <path d="M3 5h18" />
                <path d="M7 12h10" />
                <path d="M10 19h4" />
              </svg>
            </span>
            <div className="min-w-0">
              <h2 className="text-dark text-sm font-semibold">Filter</h2>
              <p className="text-muted text-xs">
                {activeFilterCount > 0
                  ? `${activeFilterCount} active ${activeFilterCount === 1 ? 'filter' : 'filters'}`
                  : 'Collapsed'}
              </p>
            </div>
            <svg
              aria-hidden="true"
              viewBox="0 0 24 24"
              className={`text-muted h-4 w-4 shrink-0 transition-transform group-hover:text-dark ${
                expanded ? 'rotate-180' : ''
              }`}
              fill="none"
              stroke="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
            >
              <path d="m6 9 6 6 6-6" />
            </svg>
          </button>

          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={resetFilters}
              className="border-border text-muted hover:bg-highlight hover:text-dark rounded-md border px-3 py-2 text-sm font-medium transition-colors"
            >
              Reset
            </button>
            <button
              type="button"
              onClick={() => setExpanded(current => !current)}
              className="bg-accent text-light hover:bg-accent/90 rounded-md px-4 py-2 text-sm font-semibold shadow-sm transition-colors"
            >
              {expanded ? 'Close' : 'Open'}
            </button>
          </div>
        </div>

        {expanded && (
          <div className="border-border bg-elevated absolute top-full right-0 left-0 mt-2 max-h-[min(28rem,calc(100vh-14rem))] overflow-auto rounded-lg border p-4 shadow-lg">
            <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 xl:grid-cols-4">
              {fields.map(field => (
                <label key={field.name} className="flex min-w-0 flex-col gap-1.5">
                  <span className="text-muted text-xs font-semibold">{field.label}</span>
                  {field.type === 'select' ? (
                    <select
                      value={draft[field.name] ?? ''}
                      onChange={event => updateDraft(field.name, event.target.value)}
                      className="border-border bg-surface text-dark focus:border-accent focus:ring-accent/20 h-10 rounded-md border px-3 text-sm outline-none transition focus:ring-2"
                    >
                      <option value="">All</option>
                      {field.options.map(option => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  ) : (
                    <input
                      type={field.type ?? 'text'}
                      value={draft[field.name] ?? ''}
                      onChange={event => updateDraft(field.name, event.target.value)}
                      placeholder={field.placeholder}
                      className="border-border bg-surface text-dark placeholder:text-muted/70 focus:border-accent focus:ring-accent/20 h-10 rounded-md border px-3 text-sm outline-none transition focus:ring-2"
                    />
                  )}
                </label>
              ))}
            </div>

            <div className="border-border mt-4 flex justify-end gap-2 border-t pt-4">
              <button
                type="button"
                onClick={resetFilters}
                className="border-border text-muted hover:bg-highlight hover:text-dark rounded-md border px-3 py-2 text-sm font-medium transition-colors"
              >
                Reset
              </button>
              <button
                type="submit"
                className="bg-accent text-light hover:bg-accent/90 rounded-md px-4 py-2 text-sm font-semibold shadow-sm transition-colors"
              >
                Apply filters
              </button>
            </div>
          </div>
        )}
      </div>
    </form>
  )
}

function cleanValues(values: FilterValues) {
  return Object.fromEntries(
    Object.entries(values)
      .map(([key, value]) => [key, value.trim()])
      .filter(([, value]) => value)
  )
}
