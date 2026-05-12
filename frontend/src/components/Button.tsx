type ButtonProps = {
  label: string
  onClick: () => void
}

export default function Button({ label, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="bg-accent text-light hover:bg-dark focus:ring-accent/25 rounded-md px-4 py-2.5 text-sm font-semibold shadow-sm transition-colors focus:ring-4 focus:outline-none active:translate-y-px"
    >
      {label}
    </button>
  )
}
