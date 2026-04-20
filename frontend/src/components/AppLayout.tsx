import { NavLink, Outlet } from 'react-router-dom'
import HomeIcon from './HomeIcon.tsx'
import NavItem from './NavItem.tsx'

const links = [
  { to: '/patients', label: 'Patients' },
  { to: '/doctors', label: 'Doctors' },
  { to: '/nurses', label: 'Nurses' },
  { to: '/departments', label: 'Departments' },
  { to: '/stations', label: 'Stations' },
  { to: '/drugs', label: 'Drugs' },
  { to: '/test', label: 'Test' },
]

export default function AppLayout() {
  return (
    <div className="min-h-screen bg-background text-text flex">
      <aside className="w-64 shrink-0 border-r border-border bg-surface p-4">
        <HomeIcon />
        <nav className="flex flex-col gap-2">
          {links.map(item => (
            <NavItem key={item.to} to={item.to} label={item.label} />
          ))}
        </nav>
      </aside>

      <main className="flex-1 p-6">
        <div className="rounded-xl border border-border bg-surface p-4">
          <Outlet />
        </div>
      </main>
    </div>
  )
}