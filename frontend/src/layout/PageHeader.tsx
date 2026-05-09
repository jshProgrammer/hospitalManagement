type PageHeaderProps = {
  title: string
}

export default function PageHeader({ title }: PageHeaderProps) {
  return (
    <header className="mb-4">
      <h1 className="text-dark rounded-xl px-4 text-3xl font-semibold">{title}</h1>
    </header>
  )
}