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
  const activeFilterCount = Object.values(values).filter(Boolean).length

  function updateDraft(name: string, value: string) {
    setDraft(current => ({ ...current, [name]: value }))
  }

  function applyFilters(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    onChange(cleanValues(draft))
  }

  function resetFilters() {
    setDraft({})
    onChange({})
  }

  return (
    <form
      onSubmit={applyFilters}
      className="border-border bg-elevated mb-4 rounded-lg border px-4 py-3 shadow-sm"
    >
      <div className="flex flex-col gap-3">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <div className="flex items-center gap-2">
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
            <div>
              <h2 className="text-dark text-sm font-semibold">Filter</h2>
              <p className="text-muted text-xs">
                {activeFilterCount > 0
                  ? `${activeFilterCount} active ${activeFilterCount === 1 ? 'filter' : 'filters'}`
                  : 'No filters active'}
              </p>
            </div>
          </div>

          <div className="flex items-center gap-2">
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
              Apply
            </button>
          </div>
        </div>

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
