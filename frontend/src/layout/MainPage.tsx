import PageHeader from './PageHeader.tsx'
import type { ReactNode } from 'react'

type MainPageProps = {
  title: string
  description?: string
  children: ReactNode
}

export default function MainPage({ title, description, children }: MainPageProps) {
  return (
    <div className="flex h-full min-h-0 flex-col">
      <PageHeader title={title} description={description} />
      <div className="min-h-0 flex-1">
        <div className="bg-surface border-border h-full overflow-hidden rounded-lg border shadow-sm">
          {children}
        </div>
      </div>
    </div>
  )
}
