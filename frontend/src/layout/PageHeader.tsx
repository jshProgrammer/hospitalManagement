type PageHeaderProps = {
  title: string
  description?: string
}

export default function PageHeader({ title, description }: PageHeaderProps) {
  return (
    <header className="mb-5 flex flex-wrap items-end justify-between gap-3">
      <div>
        <p className="text-primary text-xs font-semibold tracking-wide uppercase">
          Hospital Management
        </p>
        <h1 className="text-dark mt-1 text-2xl font-semibold">{title}</h1>
        {description ? <p className="text-muted mt-1 text-sm">{description}</p> : null}
      </div>
    </header>
  )
}
