import { type ButtonHTMLAttributes, type ReactNode } from 'react'

type ButtonVariant = 'default' | 'primary' | 'secondary'

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  label: string
  variant?: ButtonVariant
  icon?: ReactNode
}

const buttonVariants: Record<ButtonVariant, string> = {
  default:
    'text-dark hover:bg-highlight hover:border-accent active:bg-accent active:text-light border-border px-4 py-2.5 font-semibold shadow-sm active:translate-y-px',
  primary: 'bg-accent text-light hover:bg-accent/90 px-4 py-2 font-semibold shadow-sm',
  secondary: 'border-border text-muted hover:bg-highlight hover:text-dark border px-3 py-2 font-medium',
}

export default function Button({
  label,
  variant = 'default',
  type = 'button',
  className = '',
  icon,
  ...props
}: ButtonProps) {
  return (
    <button
      type={type}
      className={`inline-flex items-center justify-center gap-2 rounded-md text-sm transition-colors ${buttonVariants[variant]} ${className}`}
      {...props}
    >
      {icon}
      {label}
    </button>
  )
}
