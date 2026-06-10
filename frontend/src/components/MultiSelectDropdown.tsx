import { useMemo, useState } from 'react'
import { Check, ChevronDown } from 'lucide-react'

type MultiSelectDropdownProps = {
  options: { label: string; value: string }[]
  value: string
  onChange: (value: string) => void
  placeholder?: string
}

export default function MultiSelectDropdown({
  options,
  value,
  onChange,
  placeholder = 'All',
}: MultiSelectDropdownProps) {
  const [open, setOpen] = useState(false)
  const selectedValues = useMemo(() => new Set(value.split(',').filter(Boolean)), [value])
  const selectedLabels = options
    .filter(option => selectedValues.has(option.value))
    .map(option => option.label)

  function toggleValue(nextValue: string) {
    const nextSelectedValues = new Set(selectedValues)

    if (nextSelectedValues.has(nextValue)) {
      nextSelectedValues.delete(nextValue)
    } else {
      nextSelectedValues.add(nextValue)
    }

    onChange(Array.from(nextSelectedValues).join(','))
  }

  return (
    <div className="relative">
      <button
        type="button"
        onClick={() => setOpen(current => !current)}
        className="border-border bg-surface text-dark focus:border-accent focus:ring-accent/20 flex h-10 w-full items-center justify-between gap-2 rounded-md border px-3 text-left text-sm transition outline-none focus:ring-2"
      >
        <span className="min-w-0 truncate">
          {selectedLabels.length > 0 ? selectedLabels.join(', ') : placeholder}
        </span>
        <ChevronDown
          className={`text-muted size-4 shrink-0 transition-transform ${open ? 'rotate-180' : ''}`}
        />
      </button>

      {open && (
        <div className="border-border bg-elevated absolute z-30 mt-1 w-full overflow-hidden rounded-md border shadow-lg">
          {options.map(option => {
            const selected = selectedValues.has(option.value)

            return (
              <button
                key={option.value}
                type="button"
                onClick={() => toggleValue(option.value)}
                className="hover:bg-highlight flex w-full items-center gap-2 px-3 py-2 text-left text-sm"
              >
                <span className="border-border flex size-4 items-center justify-center rounded border">
                  {selected && <Check className="text-accent size-3" />}
                </span>
                <span className="text-dark min-w-0 flex-1 truncate">{option.label}</span>
              </button>
            )
          })}
        </div>
      )}
    </div>
  )
}
