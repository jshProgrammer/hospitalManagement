type PageHeaderProps = {
  title: string
}

export default function PageHeader({ title }: PageHeaderProps) {
  return (
    <header className="mb-6 border-b border-border pb-4">
      <h1 className="text-accent text-3xl font-semibold tracking-tight">{title}</h1>
    </header>
  )
}
