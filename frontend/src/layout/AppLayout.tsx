import { Outlet } from 'react-router-dom'
import HomeIcon from '../components/HomeIcon.tsx'
import NavItem from '../components/NavItem.tsx'
import { links } from '../routes.ts'

export default function AppLayout() {
  return (
    <div className="bg-app text-dark flex h-screen gap-4 overflow-hidden p-4">
      <aside className="border-border bg-surface flex w-72 shrink-0 flex-col rounded-lg border p-4 shadow-sm">
        <div className="flex min-h-0 flex-1 flex-col gap-5">
          <HomeIcon />
          <nav className="flex flex-col gap-1.5" aria-label="Primary navigation">
            {links.map(item => (
              <NavItem key={item.to} to={item.to} label={item.label} />
            ))}
          </nav>
        </div>
        <div className="border-border mt-6 border-t pt-4">
          <p className="text-muted text-xs font-medium uppercase">Hospital System</p>
          <p className="text-dark mt-1 text-sm font-semibold">Central Management</p>
        </div>
      </aside>

      <main className="min-w-0 flex-1 overflow-hidden">
        <div className="bg-background border-border h-full overflow-hidden rounded-lg border p-6 shadow-sm">
          <Outlet />
        </div>
      </main>
    </div>
  )
}
