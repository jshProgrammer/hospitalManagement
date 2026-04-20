import { NavLink } from 'react-router-dom'

type NavItemProps = {
  to: string
  label: string
}

export default function NavItem({ to, label }: NavItemProps) {
  return (
    <NavLink
      to={to}
      className={({ isActive }) =>
        [
          'rounded-lg px-4 py-3 text-sm font-medium shadow-sm transition hover:scale-105 hover:shadow-md active:scale-95 active:shadow-inner',
          isActive
            ? 'bg-accent-active text-dark'
            : 'text-light bg-accent hover:bg-accent-hover border-border transition hover:text-white',
        ].join(' ')
      }
    >
      {label}
    </NavLink>
  )
}