type PageHeaderProps = {
  title: string
}

export default function PageHeader({ title }: PageHeaderProps) {
  return (
    <header className="mb-6">
      <h1 className="bg-primary text-dark w-auto rounded-xl p-4 text-3xl font-semibold">{title}</h1>
    </header>
  )
}