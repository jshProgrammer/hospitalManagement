import { Outlet } from 'react-router-dom'
import HomeIcon from '../components/HomeIcon.tsx'
import NavItem from '../components/NavItem.tsx'

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
    <div className="bg-accent text-dark flex h-screen gap-4 overflow-hidden p-4">
      <aside className="border-border bg-background w-64 shrink-0 rounded-xl p-4">
        <div className="flex flex-col gap-3">
          <HomeIcon />
          <nav className="flex flex-col gap-3">
            {links.map(item => (
              <NavItem to={item.to} label={item.label} />
            ))}
          </nav>
        </div>
      </aside>

      <main className="min-w-0 flex-1">
        <div className="bg-background h-full rounded-xl p-8">
          <Outlet />
        </div>
      </main>
    </div>
  )
}