type ButtonProps = {
  label: string
  onClick: () => void
}

export default function Button({ label, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="text-dark hover:bg-highlight hover:border-accent active:bg-accent active:text-light border-border rounded-md px-4 py-2.5 text-sm font-semibold shadow-sm transition-colors active:translate-y-px"
    >
      {label}
    </button>
  )
}