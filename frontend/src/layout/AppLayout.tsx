import { Outlet } from 'react-router-dom'
import HomeIcon from '../components/HomeIcon'
import NavItem from '../components/NavItem'
import { links } from '../constants/navigation.ts'

export default function AppLayout() {
  return (
    <div className="bg-background text-dark flex h-screen gap-4 overflow-hidden p-4">
      <aside className="border-border bg-elevated w-64 shrink-0 rounded-lg border p-4 shadow-sm">
        <div className="flex h-full flex-col gap-4">
          <HomeIcon />
          <nav className="flex flex-col gap-2">
            {links.map(item => (
              <NavItem key={item.to} to={item.to} label={item.label} />
            ))}
          </nav>
        </div>
      </aside>

      <main className="min-w-0 flex-1">
        <div className="border-border bg-elevated h-full rounded-lg border p-8 shadow-sm">
          <Outlet />
        </div>
      </main>
    </div>
  )
}