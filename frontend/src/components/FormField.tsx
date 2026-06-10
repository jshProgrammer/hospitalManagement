import { useId, type InputHTMLAttributes, type SelectHTMLAttributes } from 'react'

type SelectOption = {
  label: string
  value: string
}

type BaseFieldProps = {
  label: string
  error?: string
}

type InputFieldProps = BaseFieldProps &
  InputHTMLAttributes<HTMLInputElement> & {
    type?: 'text' | 'email' | 'tel' | 'number' | 'date'
  }

type SelectFieldProps = BaseFieldProps &
  SelectHTMLAttributes<HTMLSelectElement> & {
    type: 'select'
    options: SelectOption[]
    placeholder?: string
  }

type FormFieldProps = InputFieldProps | SelectFieldProps

const controlClassName =
  'border-border bg-surface text-dark placeholder:text-muted/70 focus:border-accent focus:ring-accent/20 h-10 w-full rounded-md border px-3 text-sm transition outline-none focus:ring-2 disabled:cursor-not-allowed disabled:opacity-60'

export default function FormField(props: FormFieldProps) {
  const generatedId = useId()
  const id = props.id ?? generatedId

  if (props.type === 'select') {
    const { label, error, options, placeholder, type: selectType, ...selectProps } = props
    void selectType

    return (
      <label htmlFor={id} className="flex min-w-0 flex-col gap-1.5">
        <span className="text-muted text-xs font-semibold">{label}</span>
        <select {...selectProps} id={id} className={controlClassName}>
          <option value="">{placeholder ?? 'Select'}</option>
          {options.map(option => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
        {error && <span className="text-error text-xs">{error}</span>}
      </label>
    )
  }

  const { label, error, type = 'text', ...inputProps } = props

  return (
    <label htmlFor={id} className="flex min-w-0 flex-col gap-1.5">
      <span className="text-muted text-xs font-semibold">{label}</span>
      <input {...inputProps} id={id} type={type} className={controlClassName} />
      {error && <span className="text-error text-xs">{error}</span>}
    </label>
  )
}
