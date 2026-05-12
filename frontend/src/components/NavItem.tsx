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
          'rounded-md border px-4 py-3 text-sm font-medium transition-colors',
          isActive
            ? 'border-accent bg-accent text-light shadow-sm'
            : 'text-accent border-border/50 hover:bg-highlight',
        ].join(' ')
      }
    >
      {label}
    </NavLink>
  )
}
