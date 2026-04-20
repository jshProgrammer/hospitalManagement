import PageHeader from './PageHeader.tsx'
import type { ReactNode } from 'react'

type MainPageProps = {
  title: string
  children: ReactNode
}

export default function MainPage({ title, children }: MainPageProps) {
  return (
    <div className="flex h-full flex-col">
      <PageHeader title={title} />
      <div className="flex-1">
        <div className="bg-surface border-border h-full rounded-xl border p-4 shadow-sm">
          {children}
        </div>
      </div>
    </div>
  )
}