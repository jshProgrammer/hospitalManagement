type ButtonProps = {
  label: string
  onClick: () => void
}

export default function Button({ label, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className="text-light bg-accent hover:bg-accent-hover active:bg-accent-active transform rounded-lg px-4 py-3 text-sm font-medium shadow-sm transition hover:scale-105 hover:shadow-md active:scale-95 active:shadow-inner"
    >
      {label}
    </button>
  )
}