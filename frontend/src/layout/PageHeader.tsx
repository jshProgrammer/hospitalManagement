import type { ReactNode } from 'react'

type PageHeaderProps = {
  title: string
  actions?: ReactNode
}

export default function PageHeader({ title, actions }: PageHeaderProps) {
  return (
    <header className="border-border mb-6 flex flex-wrap items-center justify-between gap-3 border-b pb-4">
      <h1 className="text-accent text-3xl font-semibold tracking-tight">{title}</h1>
      {actions}
    </header>
  )
}
