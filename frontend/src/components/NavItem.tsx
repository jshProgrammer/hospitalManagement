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
          'rounded-lg px-4 py-3 text-sm font-medium shadow-sm transition-transform hover:scale-105 hover:shadow-md active:scale-95 active:shadow-inner',
          isActive
            ? 'bg-highlight text-dark'
            : 'text-light bg-accent hover:bg-highlight hover:text-dark border-border transition',
        ].join(' ')
      }
    >
      {label}
    </NavLink>
  )
}