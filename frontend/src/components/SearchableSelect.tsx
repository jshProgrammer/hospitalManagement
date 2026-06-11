import { useEffect, useId, useMemo, useRef, useState } from 'react'

export type SearchableSelectOption = {
  label: string
  value: string
  description?: string
  searchText?: string
}

type SearchableSelectProps = {
  label: string
  value: string
  options: SearchableSelectOption[]
  onChange: (value: string) => void
  query: string
  onQueryChange: (query: string) => void
  placeholder?: string
  emptyMessage?: string
  loading?: boolean
  required?: boolean
  disabled?: boolean
  maxVisibleOptions?: number
  filterOptions?: boolean
}

const controlClassName =
  'border-border bg-surface text-dark placeholder:text-muted/70 focus:border-accent focus:ring-accent/20 h-10 w-full rounded-md border px-3 text-sm transition outline-none focus:ring-2 disabled:cursor-not-allowed disabled:opacity-60'

export default function SearchableSelect({
  label,
  value,
  options,
  onChange,
  query,
  onQueryChange,
  placeholder = 'Start typing',
  emptyMessage = 'No matches found',
  loading = false,
  required = false,
  disabled = false,
  maxVisibleOptions = 20,
  filterOptions = true,
}: SearchableSelectProps) {
  const generatedId = useId()
  const listboxId = `${generatedId}-listbox`
  const [open, setOpen] = useState(false)
  const containerRef = useRef<HTMLDivElement>(null)

  const selectedOption = useMemo(
    () => options.find(option => option.value === value),
    [options, value]
  )

  useEffect(() => {
    if (selectedOption && query !== selectedOption.label) {
      onQueryChange(selectedOption.label)
    }
  }, [onQueryChange, query, selectedOption])

  useEffect(() => {
    function closeOnOutsideClick(event: MouseEvent) {
      if (!containerRef.current?.contains(event.target as Node)) {
        setOpen(false)
      }
    }

    document.addEventListener('mousedown', closeOnOutsideClick)
    return () => document.removeEventListener('mousedown', closeOnOutsideClick)
  }, [])

  const normalizedQuery = query.trim().toLowerCase()
  const visibleOptions = useMemo(() => {
    const matches =
      filterOptions && normalizedQuery
        ? options.filter(option => {
            const searchable = `${option.label} ${option.description ?? ''} ${option.searchText ?? ''}`
            return searchable.toLowerCase().includes(normalizedQuery)
          })
        : options

    return matches.slice(0, maxVisibleOptions)
  }, [filterOptions, maxVisibleOptions, normalizedQuery, options])

  return (
    <div ref={containerRef} className="relative flex min-w-0 flex-col gap-1.5">
      <label htmlFor={generatedId} className="text-muted text-xs font-semibold">
        {label}
      </label>
      <input
        id={generatedId}
        type="text"
        value={query}
        onChange={event => {
          onQueryChange(event.target.value)
          onChange('')
          setOpen(true)
        }}
        onFocus={() => setOpen(true)}
        onKeyDown={event => {
          if (event.key === 'Escape') {
            setOpen(false)
          }
        }}
        placeholder={placeholder}
        required={required}
        disabled={disabled}
        role="combobox"
        aria-autocomplete="list"
        aria-expanded={open}
        aria-controls={listboxId}
        className={controlClassName}
      />
      <input tabIndex={-1} value={value} onChange={() => undefined} className="sr-only" />
      {open && !disabled && (
        <div
          id={listboxId}
          role="listbox"
          className="border-border bg-elevated absolute top-full z-30 mt-1 max-h-64 w-full overflow-auto rounded-md border shadow-lg"
        >
          {loading && <div className="text-muted px-3 py-2 text-sm">Loading...</div>}
          {!loading && visibleOptions.length === 0 && (
            <div className="text-muted px-3 py-2 text-sm">{emptyMessage}</div>
          )}
          {!loading &&
            visibleOptions.map(option => (
              <button
                key={option.value}
                type="button"
                role="option"
                aria-selected={option.value === value}
                onMouseDown={event => event.preventDefault()}
                onClick={() => {
                  onChange(option.value)
                  onQueryChange(option.label)
                  setOpen(false)
                }}
                className={`hover:bg-highlight focus:bg-highlight flex w-full flex-col px-3 py-2 text-left text-sm outline-none ${
                  option.value === value ? 'bg-highlight text-dark' : 'text-dark'
                }`}
              >
                <span className="font-medium">{option.label}</span>
                {option.description && (
                  <span className="text-muted text-xs">{option.description}</span>
                )}
              </button>
            ))}
        </div>
      )}
      {required && !value && query && (
        <span className="text-muted text-xs">Choose a result from the list.</span>
      )}
    </div>
  )
}
