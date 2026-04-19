import { NavLink } from 'react-router-dom'

const links = [
  { to: '/patients', label: 'Patients' },
  { to: '/doctors', label: 'Doctors' },
  { to: '/nurses', label: 'Nurses' },
  { to: '/departments', label: 'Departments' },
  { to: '/stations', label: 'Stations' },
  { to: '/drugs', label: 'Drugs' },
]

export function Navigation() {
  return (
    <nav className="flex gap-3 p-4">
      {links.map(link => (
        <NavLink
          key={link.to}
          to={link.to}
          className={({ isActive }) => (isActive ? 'font-bold underline' : 'text-slate-700')}
        >
          {link.label}
        </NavLink>
      ))}
    </nav>
  )
}