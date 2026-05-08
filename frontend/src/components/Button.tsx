type ButtonProps = {
  label: string
  onClick: () => void
}

export default function Button({ label, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="bg-primary text-light hover:bg-accent-hover active:bg-primary rounded-lg px-4 py-2.5 text-sm font-semibold shadow-sm transition-colors"
    >
      {label}
    </button>
  )
}
