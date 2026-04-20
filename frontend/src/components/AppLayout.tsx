import { Outlet } from 'react-router-dom'
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
    <div className="bg-surface text-text flex min-h-screen rounded-xl">
      <aside className="border-border bg-background w-64 shrink-0 border-r p-4">
        <HomeIcon />
        <nav className="flex flex-col gap-2">
          {links.map(item => (
            <NavItem key={item.to} to={item.to} label={item.label} />
          ))}
        </nav>
      </aside>

      <main className="flex-1 p-6">
        <div className="border-border bg-surface rounded-xl border p-4">
          <Outlet />
        </div>
      </main>
    </div>
  )
}