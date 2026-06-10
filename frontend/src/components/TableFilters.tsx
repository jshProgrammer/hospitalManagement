import { type SubmitEvent, useState } from 'react'
import { ChevronDown, ListFilter } from 'lucide-react'
import Button from './Button'
import MultiSelectDropdown from './MultiSelectDropdown'

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

type MultiSelectFilterField = BaseFilterField & {
  type: 'multiselect'
  options: { label: string; value: string }[]
}

export type FilterField = TextFilterField | SelectFilterField | MultiSelectFilterField

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

  function applyFilters(event: SubmitEvent<HTMLFormElement>) {
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
              <ListFilter className="size-4" />
            </span>
            <div className="min-w-0">
              <h2 className="text-dark text-sm font-semibold">Filter</h2>
              <p className="text-muted text-xs">
                {activeFilterCount > 0
                  ? `${activeFilterCount} active ${activeFilterCount === 1 ? 'filter' : 'filters'}`
                  : ''}
              </p>
            </div>
            <ChevronDown
              className={`text-muted group-hover:text-dark size-4 transition-transform ${expanded ? 'rotate-180' : ''}`}
            />
          </button>

          <div className="flex items-center gap-2">
            <Button label="Reset" variant="secondary" onClick={resetFilters} />
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
                      className="border-border bg-surface text-dark focus:border-accent focus:ring-accent/20 h-10 rounded-md border px-3 text-sm transition outline-none focus:ring-2"
                    >
                      <option value="">All</option>
                      {field.options.map(option => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </select>
                  ) : field.type === 'multiselect' ? (
                    <MultiSelectDropdown
                      value={draft[field.name] ?? ''}
                      onChange={value => updateDraft(field.name, value)}
                      options={field.options}
                    />
                  ) : (
                    <input
                      type={field.type ?? 'text'}
                      value={draft[field.name] ?? ''}
                      onChange={event => updateDraft(field.name, event.target.value)}
                      placeholder={field.placeholder}
                      className="border-border bg-surface text-dark placeholder:text-muted/70 focus:border-accent focus:ring-accent/20 h-10 rounded-md border px-3 text-sm transition outline-none focus:ring-2"
                    />
                  )}
                </label>
              ))}
            </div>

            <div className="border-border mt-4 flex justify-end gap-2 border-t pt-4">
              <Button label="Reset" variant="secondary" onClick={resetFilters} />
              <Button label="Apply filters" type="submit" variant="primary" />
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
