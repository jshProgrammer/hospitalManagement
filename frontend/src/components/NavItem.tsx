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
          'group border-border flex items-center gap-3 rounded-lg border px-3 py-2.5 text-sm font-medium transition-colors',
          isActive
            ? 'bg-primary-muted text-primary border-highlight'
            : 'text-muted bg-surface hover:bg-surface-muted hover:text-dark',
        ].join(' ')
      }
    >
      <span
        className="bg-border group-aria-[current=page]:bg-primary h-2 w-2 rounded-full"
        aria-hidden="true"
      />
      {label}
    </NavLink>
  )
}
