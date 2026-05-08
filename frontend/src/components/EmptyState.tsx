type EmptyStateProps = {
  title: string
  detail: string
}

export default function EmptyState({ title, detail }: EmptyStateProps) {
  return (
    <div className="flex h-full items-center justify-center p-8 text-center">
      <div className="max-w-md">
        <div className="bg-primary-muted text-primary mx-auto mb-4 flex h-12 w-12 items-center justify-center rounded-lg text-lg font-semibold">
          +
        </div>
        <h2 className="text-dark text-lg font-semibold">{title}</h2>
        <p className="text-muted mt-2 text-sm leading-6">{detail}</p>
      </div>
    </div>
  )
}
